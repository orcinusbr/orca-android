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

package br.com.orcinus.orca.core.mastodon.notification.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
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
import br.com.orcinus.orca.core.mastodon.notification.InternalNotificationApi;
import br.com.orcinus.orca.core.mastodon.notification.service.async.Pipeline;
import br.com.orcinus.orca.core.mastodon.notification.service.async.Pipelines;
import br.com.orcinus.orca.core.mastodon.notification.service.interop.KSerializers;
import java.lang.annotation.Annotation;
import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import kotlin.Unit;
import kotlinx.serialization.ExperimentalSerializationApi;
import kotlinx.serialization.KSerializer;
import kotlinx.serialization.SerializationStrategy;
import kotlinx.serialization.descriptors.SerialDescriptor;
import kotlinx.serialization.descriptors.SerialDescriptorsKt;
import kotlinx.serialization.encoding.Decoder;
import kotlinx.serialization.encoding.DecodingKt;
import kotlinx.serialization.encoding.Encoder;
import kotlinx.serialization.encoding.EncodingKt;
import org.jetbrains.annotations.NotNull;

/** Structure returned by the API when the user is notified of an occurrence related to them. */
@InternalNotificationApi
final class MastodonNotification {
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
   * the {@link #type} is {@link Type#FAVOURITE}, {@link Type#MENTION}, {@link Type#POLL}, {@link
   * Type#REBLOG}, {@link Type#STATUS} or {@link Type#UPDATE}.
   */
  @Nullable final MastodonStatus status;

  /** {@link KSerializer} for serializing and deserializing a {@link MastodonNotification}. */
  static final class Serializer implements KSerializer<@NotNull MastodonNotification> {
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
    @NonNull static final Serializer instance = new Serializer();

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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return Pipelines.immediate(
            context.getString(
                R.string.someone_has_favorited_your_post,
                parent.account.getAcct(),
                status.getSummarizedContent()));
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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        return Pipelines.immediate(
            context.getString(R.string.someone_has_followed_you, parent.account.getAcct()));
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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        return Pipelines.immediate(
            context.getString(
                R.string.someone_has_requested_to_follow_you, parent.account.getAcct()));
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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return Pipelines.immediate(
            context.getString(
                R.string.someone_has_mentioned_you,
                parent.account.getAcct(),
                status.getSummarizedContent()));
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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        return parent
            .account
            .isOwnedAsync(authenticationLock)
            .thenApply(
                (isAccountOwned) -> {
                  final MastodonStatus status = parent.status;
                  assert status != null;
                  return isAccountOwned
                      ? context.getString(
                          R.string.poll_created_by_you_has_ended, status.getSummarizedContent())
                      : context.getString(
                          R.string.poll_created_by_someone_has_ended,
                          parent.account.getAcct(),
                          status.getSummarizedContent());
                });
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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return Pipelines.immediate(
            context.getString(
                R.string.someone_has_reposted_your_post,
                parent.account.getAcct(),
                status.getSummarizedContent()));
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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        return Pipelines.immediate(context.getString(R.string.relationship_has_been_severed));
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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return Pipelines.immediate(
            context.getString(
                R.string.someone_has_published,
                parent.account.getAcct(),
                status.getSummarizedContent()));
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
      final Pipeline<String> getContentTitleAsync(
          final Context context,
          final AuthenticationLock<?> authenticationLock,
          final MastodonNotification parent) {
        final MastodonStatus status = parent.status;
        assert status != null;
        return Pipelines.immediate(
            context.getString(
                R.string.someone_has_edited,
                parent.account.getAcct(),
                status.getSummarizedContent()));
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
    final NotificationChannel toNotificationChannel(final Context context) {
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
    final String getChannelName(final Context context) {
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
    final String getChannelDescription(final Context context) {
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
     * Creates a {@link Pipeline} in which the title of the content displayed by a {@link
     * Notification} is computed.
     *
     * @param context {@link Context} from which the {@link String} will be retrieved given its
     *     resource ID.
     * @param authenticationLock {@link AuthenticationLock} for checking whether the related {@link
     *     MastodonAccount} is owned by the current {@link Actor}.
     * @param parent {@link MastodonNotification} to which this {@link Type} belongs.
     * @see #toNotificationAsync(Context, AuthenticationLock)
     * @see #account
     * @see MastodonAccount#isOwnedAsync(AuthenticationLock)
     */
    @NonNull
    abstract Pipeline<String> getContentTitleAsync(
        final Context context,
        final AuthenticationLock<?> authenticationLock,
        final MastodonNotification parent);
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
   *     Only present when the {@link #type} is {@link Type#FAVOURITE}, {@link Type#MENTION}, {@link
   *     Type#POLL}, {@link Type#REBLOG}, {@link Type#STATUS} or {@link Type#UPDATE}.
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
  public boolean equals(final Object other) {
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
   * Converts a {@link ZonedDateTime} into a {@link #createdAt} {@link String}.
   *
   * @param zonedDateTime {@link ZonedDateTime} to be converted.
   */
  @NonNull
  @VisibleForTesting
  static String createdAt(@NonNull final ZonedDateTime zonedDateTime) {
    return zonedDateTime.format(DateTimeFormatter.ISO_INSTANT);
  }

  /**
   * Creates a version of the {@link #id} suited for a notification to be sent to the device. Given
   * that the original one is a {@link String}, it is converted into an integer in case it contains
   * only digits; otherwise, its hash code is returned.
   *
   * @return {@link #id} as an integer (if it is digit-only) or its hash code.
   */
  int generateSystemNotificationID() {
    try {
      if (!id.startsWith("0")) {
        return Integer.parseInt(id);
      }
    } catch (NumberFormatException exception) {
    }
    return id.hashCode();
  }

  /**
   * Creates a {@link Pipeline} in which a conversion of this {@link MastodonNotification} into a
   * {@link Notification} is computed.
   *
   * @param context {@link Context} with which the underlying {@link Notification.Builder} will be
   *     instantiated.
   * @param authenticationLock {@link AuthenticationLock} for checking whether the {@link #account}
   *     is owned by the current {@link Actor}.
   * @see Type#getContentTitleAsync(Context, AuthenticationLock, MastodonNotification)
   * @see #account
   * @see MastodonAccount#isOwnedAsync(AuthenticationLock)
   */
  @NonNull
  Pipeline<Notification> toNotificationAsync(
      @NonNull final Context context, @NonNull final AuthenticationLock<?> authenticationLock) {
    return type.getContentTitleAsync(context, authenticationLock, this)
        .thenApply(
            (contentTitle) ->
                new Notification.Builder(context, type.getChannelID())
                    .setAutoCancel(true)
                    .setContentTitle(contentTitle)
                    .setShowWhen(true)
                    .setWhen(Instant.parse(createdAt).toEpochMilli())
                    .build());
  }
}