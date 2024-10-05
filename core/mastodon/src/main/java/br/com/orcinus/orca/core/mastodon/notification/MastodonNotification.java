/*
 * Copyright © 2024 Orcinus
 *
 * This program is free software: you can redistribute it and/or modify it under the terms of the
 * GNU General Public License as published by the Free Software Foundation, either version 3 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See
 * the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with this program. If
 * not, see https://www.gnu.org/licenses.
 */

package br.com.orcinus.orca.core.mastodon.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.OptIn;
import androidx.annotation.StringRes;
import androidx.annotation.VisibleForTesting;
import br.com.orcinus.orca.core.auth.AuthenticationLock;
import br.com.orcinus.orca.core.auth.actor.Actor;
import br.com.orcinus.orca.core.mastodon.R;
import br.com.orcinus.orca.core.mastodon.feed.profile.account.MastodonAccount;
import br.com.orcinus.orca.core.mastodon.feed.profile.post.status.MastodonStatus;
import br.com.orcinus.orca.core.mastodon.notification.interop.CompletableContinuation;
import br.com.orcinus.orca.core.mastodon.notification.interop.CompletableContinuations;
import br.com.orcinus.orca.core.mastodon.notification.interop.KSerializers;
import br.com.orcinus.orca.core.module.CoreModule;
import br.com.orcinus.orca.std.injector.Injector;
import br.com.orcinus.orca.std.injector.module.Module;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.function.Supplier;
import kotlin.Unit;
import kotlin.collections.MapsKt;
import kotlin.coroutines.Continuation;
import kotlin.coroutines.CoroutineContext;
import kotlin.jvm.functions.Function2;
import kotlinx.coroutines.BuildersKt;
import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.CoroutineStart;
import kotlinx.coroutines.Job;
import kotlinx.serialization.ExperimentalSerializationApi;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.SerializationException;
import kotlinx.serialization.SerializationStrategy;
import kotlinx.serialization.builtins.BuiltinSerializersKt;
import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.descriptors.SerialDescriptorsKt;
import kotlinx.serialization.encoding.Decoder;
import kotlinx.serialization.encoding.DecodingKt;
import kotlinx.serialization.encoding.Encoder;
import kotlinx.serialization.encoding.EncodingKt;
import kotlinx.serialization.json.Json;
import kotlinx.serialization.json.JsonElement;

/** Structure returned by the API when the user is notified of an occurrence related to them. */
final class MastodonNotification {
  /**
   * {@link Json} based on which {@link MastodonNotification}s and {@link Map}s are converted into
   * each other.
   *
   * @see MastodonNotification#from(Map)
   * @see MastodonNotification#toMap()
   */
  private static final Json json = Json.Default;

  /** Unique identifier in the database. */
  @NonNull private final String id;

  /** Defines what this {@link MastodonNotification} is about. */
  @NonNull final Type type;

  /** ISO-8601-formatted moment in time at which the notification was delivered. */
  @NonNull final String createdAt;

  /**
   * {@link MastodonAccount} responsible for the event that resulted in this {@link
   * MastodonNotification} being sent.
   */
  @NonNull final MastodonAccount account;

  /**
   * {@link MastodonStatus} to which this {@link MastodonNotification} is related. Only present when
   * the {@link MastodonNotification#type} is {@link Type#FAVOURITE}, {@link Type#MENTION}, {@link
   * Type#POLL}, {@link Type#REBLOG}, {@link Type#STATUS} or {@link Type#UPDATE}.
   */
  @Nullable final MastodonStatus status;

  /** {@link KSerializer} for serializing and deserializing a {@link MastodonNotification}. */
  static final class Serializer implements KSerializer<MastodonNotification> {
    /** Ordered {@link Element}s that map to the fields of a {@link MastodonNotification}. */
    @VisibleForTesting
    static Element[] elements =
        new Element[] {
          new Element("id", KSerializers.string()),
          new Element("type", Type.Serializer.instance),
          new Element("createdAt", KSerializers.string()),
          new Element("account", KSerializers.mastodonAccount()),
          new Element("status", KSerializers.mastodonStatus())
        };

    /** {@link SerialDescriptor} that describes the structure of a {@link MastodonNotification}. */
    @NonNull
    @OptIn(markerClass = ExperimentalSerializationApi.class)
    private final SerialDescriptor descriptor =
        SerialDescriptorsKt.buildClassSerialDescriptor(
            Serializer.class.getSimpleName(),
            new SerialDescriptor[] {},
            (builder) -> {
              final List<Annotation> annotations = List.of();
              final boolean isOptional = false;
              for (Element element : elements) {
                builder.element(
                    element.name, element.strategy.getDescriptor(), annotations, isOptional);
              }
              return Unit.INSTANCE;
            });

    /** Single instance of a {@link Serializer}. */
    @NonNull @VisibleForTesting static final Serializer instance = new Serializer();

    /** Unit which composes the description of a {@link MastodonNotification}. */
    static final class Element {
      /** Name of the field to which this {@link Element} refers. */
      @NonNull final String name;

      /** {@link SerializationStrategy} for serializing the value. */
      @NonNull final SerializationStrategy<?> strategy;

      /**
       * Unit which composes the description of a {@link MastodonNotification}.
       *
       * @param name Name of the field to which this {@link Element} refers.
       * @param strategy {@link SerialDescriptor} that describes the structure of the value.
       */
      private Element(
          @NonNull final String name, @NonNull final SerializationStrategy<?> strategy) {
        this.name = name;
        this.strategy = strategy;
      }
    }

    /** {@link KSerializer} for serializing and deserializing a {@link MastodonNotification}. */
    private Serializer() {}

    @NonNull
    @Override
    public SerialDescriptor getDescriptor() {
      return descriptor;
    }

    @Override
    public void serialize(@NonNull final Encoder encoder, final MastodonNotification value) {
      EncodingKt.encodeStructure(
          encoder,
          getDescriptor(),
          (compositeEncoder) -> {
            final Object[] objects =
                new Object[] {value.id, value.type, value.createdAt, value.account, value.status};
            for (int index = 0; index < objects.length; index++) {
              final Element element = elements[index];
              final Object object = objects[index];

              //noinspection unchecked
              compositeEncoder.encodeSerializableElement(
                  getDescriptor(), index, (SerializationStrategy<Object>) element.strategy, object);
            }
            return Unit.INSTANCE;
          });
    }

    @NonNull
    @Override
    public MastodonNotification deserialize(@NonNull final Decoder decoder) {
      return DecodingKt.decodeStructure(
          decoder,
          getDescriptor(),
          (compositeDecoder) -> {
            final Supplier<Integer> indexSupplier =
                () -> compositeDecoder.decodeElementIndex(getDescriptor());
            final String id =
                compositeDecoder.decodeStringElement(getDescriptor(), indexSupplier.get());
            final Type type =
                compositeDecoder.decodeSerializableElement(
                    getDescriptor(),
                    indexSupplier.get(),
                    Type.Serializer.instance,
                    /* previousValue= */ null);
            final String createdAt =
                compositeDecoder.decodeStringElement(getDescriptor(), indexSupplier.get());
            final MastodonAccount account =
                compositeDecoder.decodeSerializableElement(
                    getDescriptor(),
                    indexSupplier.get(),
                    KSerializers.mastodonAccount(),
                    /* previousValue= */ null);
            final MastodonStatus status =
                compositeDecoder.decodeSerializableElement(
                    getDescriptor(),
                    indexSupplier.get(),
                    KSerializers.mastodonStatus(),
                    /* previousValue= */ null);
            return new MastodonNotification(id, type, createdAt, account, status);
          });
    }
  }

  /** Denotes the topic of a {@link MastodonNotification}. */
  enum Type {
    /** Someone has favorited a {@link MastodonStatus} authored by the user. */
    FAVOURITE {
      @NonNull
      @Override
      final String getChannelID() {
        return "favoriting";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.favoriting_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.favoriting_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletableFuture<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return CompletableFuture.completedFuture(
            context.getString(
                R.string.someone_has_favorited_your_post,
                parent.account.getAcct(),
                status.getSummarizedContent()));
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return false;
      }
    },

    /** Someone has followed the user. */
    FOLLOW {
      @NonNull
      @Override
      final String getChannelID() {
        return "follows";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.follows_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.follows_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletableFuture<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        return CompletableFuture.completedFuture(
            context.getString(R.string.someone_has_followed_you, parent.account.getAcct()));
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return false;
      }
    },

    /** Someone has requested to follow the user. */
    FOLLOW_REQUEST {
      @NonNull
      @Override
      final String getChannelID() {
        return "follow-requests";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.follow_requests_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.follow_requests_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletableFuture<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        return CompletableFuture.completedFuture(
            context.getString(
                R.string.someone_has_requested_to_follow_you, parent.account.getAcct()));
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return false;
      }
    },

    /** Someone has mentioned the user. */
    MENTION {
      @NonNull
      @Override
      final String getChannelID() {
        return "mentions";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.mentions_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.mentions_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletableFuture<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return CompletableFuture.completedFuture(
            context.getString(
                R.string.someone_has_mentioned_you,
                parent.account.getAcct(),
                status.getSummarizedContent()));
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return false;
      }
    },

    /** A poll that has either been created or voted in by the user has come to an end. */
    POLL {
      @NonNull
      @Override
      final String getChannelID() {
        return "polling";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.polling_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.polling_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletionStage<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        final CompletableContinuation<Boolean> pollOwnershipContinuation =
            new CompletableContinuation<>();
        CompletableContinuations.resume(
            pollOwnershipContinuation,
            (Boolean) parent.account.isOwned(authenticationLock, pollOwnershipContinuation));
        return pollOwnershipContinuation
            .getCompletionStage()
            .thenApply(
                (isPollOwned) -> {
                  assert status != null;
                  return isPollOwned
                      ? context.getString(
                          R.string.poll_created_by_you_has_ended, status.getSummarizedContent())
                      : context.getString(
                          R.string.poll_created_by_someone_has_ended,
                          parent.account.getAcct(),
                          status.getSummarizedContent());
                });
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return true;
      }
    },

    /** Someone has reposted a {@link MastodonStatus} authored by the user. */
    REBLOG {
      @NonNull
      @Override
      final String getChannelID() {
        return "reposting";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.reposting_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.reposting_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletableFuture<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return CompletableFuture.completedFuture(
            context.getString(
                R.string.someone_has_reposted_your_post,
                parent.account.getAcct(),
                status.getSummarizedContent()));
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return false;
      }
    },

    /** Someone the user follows has been severed, either because of moderation or blocking. */
    SEVERED_RELATIONSHIPS {
      @NonNull
      @Override
      final String getChannelID() {
        return "moderation";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.moderation_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.moderation_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletableFuture<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        return CompletableFuture.completedFuture(
            context.getString(R.string.relationship_has_been_severed));
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return false;
      }
    },

    /** Someone whose {@link MastodonStatus}es the user chose to be notified about has published. */
    STATUS {
      @NonNull
      @Override
      final String getChannelID() {
        return "subscriptions";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.subscriptions_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.subscriptions_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletableFuture<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return CompletableFuture.completedFuture(
            context.getString(
                R.string.someone_has_published,
                parent.account.getAcct(),
                status.getSummarizedContent()));
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return false;
      }
    },

    /** A {@link MastodonStatus} with which the user has interacted has been edited. */
    UPDATE {
      @NonNull
      @Override
      final String getChannelID() {
        return "editing";
      }

      @Override
      final int getChannelNameResourceID() {
        return R.string.editing_notification_channel;
      }

      @Override
      final int getChannelDescriptionResourceID() {
        return R.string.editing_notification_channel_description;
      }

      @NonNull
      @Override
      final CompletableFuture<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final CoroutineScope coroutineScope,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return CompletableFuture.completedFuture(
            context.getString(
                R.string.someone_has_edited,
                parent.account.getAcct(),
                status.getSummarizedContent()));
      }

      @Override
      final boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired() {
        return false;
      }
    };

    /** {@link KSerializer} for serializing and deserializing a {@link Type}. */
    static final class Serializer implements KSerializer<Type> {
      /** Single instance of a {@link Serializer}. */
      private static final Serializer instance = new Serializer();

      /** {@link SerialDescriptor} that describes the structure of a {@link Type}. */
      @NonNull
      @OptIn(markerClass = ExperimentalSerializationApi.class)
      private final SerialDescriptor descriptor =
          SerialDescriptorsKt.buildClassSerialDescriptor(
              Serializer.class.getSimpleName(),
              new SerialDescriptor[] {},
              (builder) -> {
                final List<Annotation> annotations = List.of();
                final boolean isOptional = false;
                for (final Type type : values()) {
                  builder.element(
                      type.name(), KSerializers.string().getDescriptor(), annotations, isOptional);
                }
                return Unit.INSTANCE;
              });

      /** {@link KSerializer} for serializing and deserializing a {@link Type}. */
      private Serializer() {}

      @NonNull
      @Override
      public SerialDescriptor getDescriptor() {
        return descriptor;
      }

      @Override
      public void serialize(@NonNull final Encoder encoder, final Type value) {
        encoder.encodeEnum(getDescriptor(), List.of(values()).indexOf(value));
      }

      @NonNull
      @Override
      public Type deserialize(@NonNull final Decoder decoder) {
        return values()[decoder.decodeEnum(getDescriptor())];
      }
    }

    /**
     * Converts this {@link Type} into a {@link NotificationChannel}.
     *
     * @param context {@link Context} from which the name and the description of the {@link
     *     NotificationChannel} will be obtained.
     * @see NotificationChannel#getName()
     * @see NotificationChannel#getDescription()
     * @see Type#getChannelName(Context)
     * @see Type#getChannelDescription(Context)
     */
    @NonNull
    final NotificationChannel toNotificationChannel(Context context) {
      final NotificationChannel channel =
          new NotificationChannel(
              getChannelID(), getChannelName(context), NotificationManager.IMPORTANCE_DEFAULT);
      channel.setDescription(getChannelDescription(context));
      return channel;
    }

    /**
     * Obtains the ID of the {@link NotificationChannel} into which this {@link Type} can be
     * converted.
     *
     * @see NotificationChannel#getId()
     * @see Type#toNotificationChannel(Context)
     */
    @NonNull
    abstract String getChannelID();

    /**
     * Obtains the name of the {@link NotificationChannel} into which this {@link Type} can be
     * converted.
     *
     * @see Type#getChannelID()
     * @see NotificationChannel#getName()
     * @see Type#toNotificationChannel(Context)
     */
    @NonNull
    @VisibleForTesting
    final String getChannelName(Context context) {
      return context.getString(getChannelNameResourceID());
    }

    /**
     * Obtains the description of the {@link NotificationChannel} into which this {@link Type} can
     * be converted.
     *
     * @see Type#getChannelDescriptionResourceID()
     * @see NotificationChannel#getDescription()
     * @see Type#toNotificationChannel(Context)
     */
    @NonNull
    @VisibleForTesting
    final String getChannelDescription(Context context) {
      return context.getString(getChannelDescriptionResourceID());
    }

    /**
     * Obtains the {@link String} resource ID of the name of the {@link NotificationChannel} into
     * which this {@link Type} can be converted.
     *
     * @see NotificationChannel#getName()
     * @see Type#toNotificationChannel(Context)
     */
    @StringRes
    abstract int getChannelNameResourceID();

    /**
     * Obtains the resource ID of the {@link String} that explains the kind of notifications that
     * are sent to the {@link NotificationChannel} into which this {@link Type} can be converted.
     *
     * @see NotificationChannel#getDescription()
     * @see Type#toNotificationChannel(Context)
     */
    @StringRes
    abstract int getChannelDescriptionResourceID();

    /**
     * Obtains a {@link CompletionStage} of the title of the content displayed by a {@link
     * Notification}.
     *
     * @param context {@link Context} from which the {@link String} will be retrieved given its
     *     resource ID.
     * @param authenticationLock {@link AuthenticationLock} for checking whether the related {@link
     *     MastodonAccount} is owned by the current {@link Actor}.
     * @param coroutineScope {@link CoroutineScope} in which whether the {@link MastodonAccount} is
     *     owned or not will be fetched.
     * @param parent {@link MastodonNotification} to which this {@link Type} belongs.
     * @see MastodonNotification#toNotificationAsync(Context, AuthenticationLock, CoroutineScope)
     * @see MastodonNotification#account
     * @see MastodonAccount#isOwned(AuthenticationLock, Continuation)
     */
    @NonNull
    abstract CompletionStage<String> getContentTitleAsync(
        final Context context,
        final AuthenticationLock<?> authenticationLock,
        final CoroutineScope coroutineScope,
        final MastodonNotification parent);

    /**
     * Whether a {@link CoreModule} needs to be registered in the {@link Injector} for the content
     * title of a {@link Notification} to be obtained. If it does and it has not been by the time
     * {@link Type#getContentTitleAsync(Context, AuthenticationLock, CoroutineScope,
     * MastodonNotification)} is called, a {@link Module.DependencyNotInjectedException} is
     * guaranteed to be thrown.
     */
    @VisibleForTesting
    abstract boolean isCoreModuleRegistrationUponContentTitleObtainanceRequired();
  }

  /**
   * Structure returned by the API when the user is notified of an occurrence related to them.
   *
   * @param id Unique identifier in the database.
   * @param type Defines what it is about.
   * @param createdAt ISO-8601-formatted moment in time at which the notification was delivered.
   * @param account {@link MastodonAccount} responsible for the event that resulted in this {@link
   *     MastodonNotification} being sent.
   * @param status {@link MastodonStatus} to which this {@link MastodonNotification} is related.
   *     Only present when the {@link MastodonNotification#type} is {@link Type#FAVOURITE}, {@link
   *     Type#MENTION}, {@link Type#POLL}, {@link Type#REBLOG}, {@link Type#STATUS} or {@link
   *     Type#UPDATE}.
   */
  MastodonNotification(
      @NonNull final String id,
      @NonNull final Type type,
      @NonNull final String createdAt,
      @NonNull final MastodonAccount account,
      @Nullable final MastodonStatus status) {
    this.id = id;
    this.type = type;
    this.createdAt = createdAt;
    this.account = account;
    this.status = status;
  }

  @Override
  public boolean equals(Object other) {
    return other instanceof MastodonNotification
        && id.equals(((MastodonNotification) other).id)
        && type.equals(((MastodonNotification) other).type)
        && createdAt.equals(((MastodonNotification) other).createdAt)
        && account.equals(((MastodonNotification) other).account)
        && Objects.equals(status, ((MastodonNotification) other).status);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, type, createdAt, account, status);
  }

  @NonNull
  @Override
  public String toString() {
    return "MastodonNotification(id="
        + id
        + ", type="
        + type
        + ", createdAt="
        + createdAt
        + ", account="
        + account
        + ", status="
        + status
        + ')';
  }

  /**
   * Converts a {@link ZonedDateTime} into a {@link MastodonNotification#createdAt} {@link String}.
   *
   * @param zonedDateTime {@link ZonedDateTime} to be converted.
   */
  @NonNull
  @VisibleForTesting
  static String createdAt(@NonNull final ZonedDateTime zonedDateTime) {
    return zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
  }

  /**
   * Creates a {@link MastodonNotification} from a {@link Map}.
   *
   * @param map {@link Map} containing the name of the fields as keys to their {@link
   *     String}-converted values.
   * @see MastodonNotification#toMap()
   * @see JsonElements#toNonLiteralString(JsonElement)
   * @throws SerializationException If the {@link Map} contains a key that does not match a {@link
   *     MastodonNotification} field or one of the {@link JsonElement} {@link String} values are
   *     malformed (e. g., some of its nested or non-nested children are non-literal {@link
   *     String}s).
   */
  @NonNull
  static MastodonNotification from(@NonNull final Map<String, String> map)
      throws SerializationException {
    final Map<String, String> rearrangedMap = rearrangeOrReturn(map);
    final KSerializer<Map<String, String>> mapSerializer =
        BuiltinSerializersKt.MapSerializer(KSerializers.string(), KSerializers.string());
    final JsonElement jsonElement = json.encodeToJsonElement(mapSerializer, rearrangedMap);
    return json.decodeFromString(Serializer.instance, JsonElements.toNonLiteralString(jsonElement));
  }

  /**
   * Obtains a normalized version of the {@link MastodonNotification#id}. Given that the original
   * one is a {@link String}, it is converted into an integer in case it contains only digits;
   * otherwise, its hash code is returned.
   *
   * @return {@link MastodonNotification#id} as an integer (if it is digit-only) or its hash code.
   */
  int getNormalizedID() {
    return TextUtils.isDigitsOnly(id) ? Integer.parseInt(id) : id.hashCode();
  }

  /**
   * Converts this {@link MastodonNotification} into a {@link Map} whose values are literal {@link
   * JsonElement} {@link String}s.
   *
   * @see JsonElements#toNonLiteralString(JsonElement)
   * @see MastodonNotification#from(Map)
   */
  @NonNull
  Map<String, String> toMap() {
    final JsonElement jsonElement = json.encodeToJsonElement(Serializer.instance, this);
    final KSerializer<Map<String, JsonElement>> mapSerializer =
        BuiltinSerializersKt.MapSerializer(
            KSerializers.string(), JsonElement.Companion.serializer());
    return MapsKt.mapValues(
        json.decodeFromJsonElement(mapSerializer, jsonElement),
        (entry) -> entry.getValue().toString());
  }

  /**
   * Obtains a {@link CompletionStage} of a conversion of this {@link MastodonNotification} into a
   * {@link Notification}.
   *
   * @param context {@link Context} with which the underlying {@link Notification.Builder} will be
   *     instantiated.
   * @param authenticationLock {@link AuthenticationLock} for checking whether the {@link
   *     MastodonNotification#account} is owned by the current {@link Actor}.
   * @param coroutineScope {@link CoroutineScope} in which the {@link Job} for obtaining the title
   *     of the content will be launched.
   * @see Type#getContentTitleAsync(Context, AuthenticationLock, CoroutineScope,
   *     MastodonNotification)
   * @see BuildersKt#launch(CoroutineScope, CoroutineContext, CoroutineStart, Function2)
   * @see MastodonNotification#account
   * @see MastodonAccount#isOwned(AuthenticationLock, Continuation)
   */
  @NonNull
  CompletionStage<Notification> toNotificationAsync(
      @NonNull final Context context,
      @NonNull final AuthenticationLock<?> authenticationLock,
      @NonNull final CoroutineScope coroutineScope) {
    return type.getContentTitleAsync(context, authenticationLock, coroutineScope, this)
        .thenApply(
            (contentTitle) ->
                new Notification.Builder(context, type.getChannelID())
                    .setAutoCancel(true)
                    .setContentTitle(contentTitle)
                    .setShowWhen(true)
                    .setWhen(Instant.parse(createdAt).toEpochMilli())
                    .build());
  }

  /**
   * In case the given {@link Map} representing the structure of a {@link MastodonNotification} is
   * unordered, creates a copy of it whose entries are reordered to match the order in which they
   * are respectively serialized and deserialized.
   *
   * @param map {@link Map} to be rearranged.
   * @see Serializer#serialize(Encoder, MastodonNotification)
   * @see Serializer#deserialize(Decoder)
   * @return The {@link Map} with its entries arranged according to {@link MastodonNotification}'s
   *     serialization order, or the original one if it is already correctly ordered or is missing a
   *     value.
   */
  private static Map<String, String> rearrangeOrReturn(final Map<String, String> map) {
    final List<String> actualKeys = new ArrayList<>(map.keySet());
    int expectedKeyIndex = 0;
    for (final Serializer.Element element : Serializer.elements) {
      final String actualKey;
      try {
        actualKey = actualKeys.get(expectedKeyIndex++);
      } catch (IndexOutOfBoundsException exception) {
        // A key is missing; exiting early so that the serialization exception is thrown externally.
        break;
      }
      final boolean isUnordered = !element.name.contentEquals(actualKey);
      if (isUnordered) {
        return rearrange(map);
      }
    }
    return map;
  }

  /**
   * Copies the given {@link Map} representing the structure of a {@link MastodonNotification},
   * returning one whose entries are arranged according to the order in which they are both
   * serialized and deserialized.
   *
   * @param map {@link Map} to be rearranged.
   * @see Serializer#serialize(Encoder, MastodonNotification)
   * @see Serializer#deserialize(Decoder)
   */
  private static Map<String, String> rearrange(final Map<String, String> map) {
    final Map<String, String> rearrangedMap = new LinkedHashMap<>(map.size());
    for (final Serializer.Element element : Serializer.elements) {
      final String actualValue = map.get(element.name);
      if (actualValue == null) {
        // As in rearrangeOrReturn(Map), exiting for the caller to throw a serialization exception.
        break;
      }
      rearrangedMap.put(element.name, actualValue);
    }
    return rearrangedMap;
  }
}
