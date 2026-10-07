#!/usr/bin/env bash
set -euo pipefail
ROOT="$(cd "$(dirname "$0")" && pwd)"
SDK="$ROOT/.android-sdk"
GRADLE="$ROOT/.gradle-local"
JAVA_BIN="$(command -v java)"
export JAVA_HOME="$(dirname "$(dirname "$(readlink -f "$JAVA_BIN")")")"
export ANDROID_HOME="$SDK"
export ANDROID_SDK_ROOT="$SDK"
export PATH="$SDK/cmdline-tools/latest/bin:$SDK/platform-tools:$GRADLE/gradle-8.9/bin:$PATH"
mkdir -p "$SDK/cmdline-tools" "$GRADLE"
if [ ! -x "$SDK/cmdline-tools/latest/bin/sdkmanager" ]; then
  TMP="$(mktemp -d)"
  curl -L --fail --retry 3 -o "$TMP/tools.zip" "https://dl.google.com/android/repository/commandlinetools-linux-11076708_latest.zip"
  rm -rf "$SDK/cmdline-tools/latest"
  mkdir -p "$SDK/cmdline-tools/latest"
  unzip -q "$TMP/tools.zip" -d "$TMP/unpacked"
  cp -R "$TMP/unpacked/cmdline-tools/." "$SDK/cmdline-tools/latest/"
  rm -rf "$TMP"
fi
yes | sdkmanager --licenses >/dev/null 2>&1 || true
sdkmanager "platform-tools" "platforms;android-35" "build-tools;35.0.0" >/dev/null
if [ ! -x "$GRADLE/gradle-8.9/bin/gradle" ]; then
  TMP="$(mktemp -d)"
  curl -L --fail --retry 3 -o "$TMP/gradle.zip" "https://services.gradle.org/distributions/gradle-8.9-bin.zip"
  unzip -q "$TMP/gradle.zip" -d "$GRADLE"
  rm -rf "$TMP"
fi
cd "$ROOT/android"
"$GRADLE/gradle-8.9/bin/gradle" clean assembleDebug --no-daemon
echo "BUILD SUCCESSFUL"
echo "APK: $ROOT/android/app/build/outputs/apk/debug/app-debug.apk"
