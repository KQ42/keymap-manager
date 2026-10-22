# Publishing to the JetBrains Marketplace

How to register and release **Keymap Manager** (`de.kq42.plugins.keymapmanager`) on the
[JetBrains Marketplace](https://plugins.jetbrains.com). All Gradle commands run from `keymap-plugin/`.

## What's already set up

- **Metadata** in `src/main/resources/META-INF/plugin.xml`: unique `<id>`, `<name>`, `<vendor>` (email + url),
  an in-IDE `<description>` (purpose + usage, shown in Settings → Plugins — not marketing copy),
  `<change-notes>`, and `<idea-version since-build="261">` (no `until-build`, so it stays compatible with
  future IDEs).
- **Marketplace listing copy** in [`docs/marketplace-description.md`](docs/marketplace-description.md) —
  the Marketplace Description field takes **Markdown**; paste the whole file. It's the "why install this"
  pitch, not the in-IDE help text. It only stays independent of `plugin.xml` because the listing's
  *General Information → Preserve changes from UI* is set to **Use changes from UI**: with the default
  (*Use data from plugin.xml*) every upload overwrites the Description with `plugin.xml`'s `<description>`.
- **Listing fields set on the web** (Marketplace → plugin → Overview → Edit Section; not in `plugin.xml`):
  tags *Productivity* + *OS Integration*, privacy policy `https://kq42.de/datenschutz/`, copyright
  `© 2026 KQ42`, documentation URL `https://kq42.de/plugins/keymap-manager/`, bug tracker = GitHub issues,
  and a plain-text *Getting Started* (that field renders no Markdown).
- **Icon**: `pluginIcon.svg` + `pluginIcon_dark.svg` in `META-INF/`.
- **Gradle publishing**: `publishPlugin` reads a Marketplace token from `PUBLISH_TOKEN`; optional author
  signing (`signPlugin`) activates only when the signing env vars are present.
- **Compatibility**: `verifyPlugin` checks the latest build of every supported major version plus the
  current EAP, selected on each run (`./gradlew printProductsReleases` lists them).

## Prerequisites (one-time)

1. **JetBrains Account** — sign in at <https://account.jetbrains.com> (the same account used for the IDE).
2. **Marketplace sign-in** — sign in at <https://plugins.jetbrains.com>; on the first upload you must accept
   the **Marketplace Developer Agreement**.
3. **(Optional) Vendor profile** — to list the plugin under an organization ("KQ42") rather than your
   personal name, create a Vendor on Marketplace and publish under it. This should match the `<vendor>` in
   `plugin.xml`.

## Build and check before every release

```bash
cd keymap-plugin
./gradlew buildPlugin      # → build/distributions/Keymap-Manager-<version>.zip
./gradlew verifyPlugin     # verdict under build/reports/pluginVerifier/<IDE>/…/verification-verdict.txt
```

Confirm the verdict is **Compatible** before uploading.

## First release — manual web upload (required)

The **first version of a plugin always goes through manual moderation** by JetBrains; this cannot be done
with `publishPlugin`.

1. Go to <https://plugins.jetbrains.com> → your profile → **Upload plugin**.
2. Upload `keymap-plugin/build/distributions/Keymap-Manager-<version>.zip`.
3. Set the **License** (MIT, per `LICENSE`) and pick the **Tags** *Productivity* and *OS Integration*
   ([spec 0005](spec/0005-first-marketplace-release.md) D3; editable later). *Keymap* is a privileged tag
   that only JetBrains assigns — ask the reviewers for it, since the plugin bundles a keymap.
4. Submit. Marketplace runs its automated Plugin Verifier and a reviewer checks the submission
   (typically ~2 business days). Once approved, the plugin page goes public.
5. In the listing (Marketplace → your plugin → Overview), set *Preserve changes from UI* to **Use
   changes from UI** first, then paste [`docs/marketplace-description.md`](docs/marketplace-description.md)
   into the Description and fill in the other web-only fields listed above. (Done for 1.8.x on
   2026-10-22, before approval — the fields are editable while the plugin is under review.)

## Subsequent releases — automated

After the first release is approved, ship updates from the command line:

1. Bump `version` in `keymap-plugin/build.gradle.kts`.
2. Update `<change-notes>` in `plugin.xml`.
3. `./gradlew buildPlugin verifyPlugin` and confirm *Compatible*.
4. Generate a **permanent token**: <https://plugins.jetbrains.com/author/me/tokens>.
5. Publish:

   ```bash
   cd keymap-plugin
   PUBLISH_TOKEN=<your-token> ./gradlew publishPlugin
   ```

   This uploads to the **stable** channel. Post-first-release updates publish after automated checks
   (no manual moderation).

### Optional: sign the artifact yourself

Marketplace signs plugins on its side, so author signing is optional. To sign locally, set all three env
vars before `publishPlugin` (or `signPlugin`):

```bash
export CERTIFICATE_CHAIN="$(cat chain.crt)"
export PRIVATE_KEY="$(cat private.pem)"
export PRIVATE_KEY_PASSWORD="…"
```

See the [plugin signing guide](https://plugins.jetbrains.com/docs/intellij/plugin-signing.html) for
generating the key/chain.

## Release checklist

- [ ] `version` bumped in `build.gradle.kts`
- [ ] `<change-notes>` updated in `plugin.xml`
- [ ] `./gradlew buildPlugin verifyPlugin` → **Compatible**
- [ ] First release: uploaded on the web and approved · Updates: `publishPlugin` with `PUBLISH_TOKEN`
- [ ] `docs/marketplace-description.md` still matches reality; re-paste into the listing if it changed
- [ ] Git tag / commit for the released version

## References

- [Publishing a plugin](https://plugins.jetbrains.com/docs/intellij/publishing-plugin.html)
- [Marketplace publishing (Gradle)](https://plugins.jetbrains.com/docs/intellij/deployment.html)
- [Plugin verifier](https://plugins.jetbrains.com/docs/intellij/verifying-plugin-compatibility.html)
