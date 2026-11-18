#!/usr/bin/env bash
# Локальная проверка перед push — те же шаги, что tests.yml, на ветке, слитой с актуальным origin/main.
#
#   scripts/check.sh            текущая ветка + origin/main
#   scripts/check.sh --no-merge  ветка как есть
#
# Работает в отдельной рабочей копии (~/.drawing-core-check): ваша папка и незакоммиченное не трогаются,
# проверяется закоммиченное состояние HEAD. На macOS дополнительно — iOS-симулятор.
set -euo pipefail

merge=1
[ "${1:-}" = "--no-merge" ] && merge=0

root=$(git rev-parse --show-toplevel)
wt="${DRAWING_CORE_CHECK_HOME:-$HOME/.drawing-core-check}"
head=$(git -C "$root" rev-parse HEAD)

step() { printf '\n\033[1m▶ %s\033[0m\n' "$*"; }

[ -n "$(git -C "$root" status --porcelain)" ] && echo "⚠ есть незакоммиченные изменения — они не проверяются"

step "Рабочая копия: HEAD $(git -C "$root" rev-parse --short HEAD)"
git -C "$root" fetch -q origin main
git -C "$root" worktree remove --force "$wt" 2>/dev/null || rm -rf "$wt"
git -C "$root" worktree add -q --detach "$wt" "$head"
cd "$wt"

if [ "$merge" = 1 ] && ! git merge-base --is-ancestor origin/main HEAD; then
  step "Слияние с origin/main $(git rev-parse --short origin/main)"
  git -c user.name=check -c user.email=check@local merge -q --no-edit origin/main \
    || { echo "✗ конфликт с main — обновите ветку"; git diff --name-only --diff-filter=U; exit 1; }
fi

# local.properties в git нет: путь к Android SDK — из вашей копии, ANDROID_HOME или стандартной папки macOS
if [ -f "$root/local.properties" ]; then
  cp "$root/local.properties" local.properties
elif [ -z "${ANDROID_HOME:-}" ] && [ -d "$HOME/Library/Android/sdk" ]; then
  echo "sdk.dir=$HOME/Library/Android/sdk" > local.properties
fi
[ -f local.properties ] || [ -n "${ANDROID_HOME:-}" ] \
  || { echo "✗ Android SDK не найден: задайте ANDROID_HOME или sdk.dir в local.properties"; exit 1; }

step "Тесты: jvm, js (Node), Android"
tasks="jvmTest jsNodeTest testAndroidHostTest jsBrowserProductionLibraryDistribution"
[ "$(uname)" = "Darwin" ] && tasks="$tasks iosSimulatorArm64Test"
./gradlew $tasks --continue

step "TypeScript: .d.ts фасада и запуск в Node"
npx --yes -p typescript@5.9.3 tsc -p ts-check
node ts-check/smoke.mjs

[ "$(uname)" = "Darwin" ] || echo "ℹ iOS не проверялся (нужен macOS) — его проверит CI"
printf '\n\033[32m✓ всё зелёное\033[0m (%s%s)\n' "$(git rev-parse --short "$head")" "$([ "$merge" = 1 ] && echo " + main $(git rev-parse --short origin/main)")"
