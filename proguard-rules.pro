#
# Copyright © 2023–2025 Orcinus
#
# This program is free software: you can redistribute it and/or modify it under the terms of the
# GNU General Public License as published by the Free Software Foundation, either version 3 of the
# License, or (at your option) any later version.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without
# even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
# General Public License for more details.
#
# You should have received a copy of the GNU General Public License along with this program. If
# not, see https://www.gnu.org/licenses.
#

#noinspection ShrinkerUnresolvedReference

-dontobfuscate
-dontwarn androidx.compose.runtime.internal.ComposableFunction2
-keep public class br.com.orcinus.orca.** { *; }
-keep public final class lombok.Generated
-keep public final class lombok.NonNull
-keepclassmembers final class * implements android.os.Parcelable {
  #noinspection ShrinkerUnresolvedReference

  public static final android.os.Parcelable$Creator CREATOR;
}
-keepclassmembers class br.com.orcinus.orca.core.mastodon.notification.** { *; }
-keepclassmembers final class br.com.orcinus.orca.composite.timeline.composition.ErrorDelegate {
  java.lang.CharSequence getError();
  void toggle(java.lang.CharSequence);
}
-keepclassmembers public enum net.time4j.** { public static final **[] values(); }
