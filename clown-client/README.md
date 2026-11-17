# CLOWN CLIENT

A client-side Fabric utility and accessibility mod for **Minecraft Java 1.21.11**. Intended for single-player, creative testing, and private servers where everyone involved has agreed to the modifications.

## Compatibility

| Component | Configured version | Basis |
|---|---:|---|
| Minecraft | `1.21.11` | Fixed target |
| Java | `21` | Minecraft 1.21.x toolchain |
| Fabric Loader | `0.18.1` minimum | Fabric 1.21.11 release baseline |
| Fabric Loom | `1.14-SNAPSHOT` | Fabric 1.21.11 guidance |
| Fabric API | `0.141.1+1.21.11` | Fabric 1.21.11 reference build |
| Mappings | Official Mojang mappings | Fabric's recommended mapping line |

These are configured starting points; remote dependency resolution and an actual game launch have not been verified here. See [COMPATIBILITY.md](COMPATIBILITY.md).

## Included foundation

- Client-only Fabric entrypoint, targeting exactly Minecraft 1.21.11.
- Right Shift opens the crimson/charcoal Click GUI.
- Category navigation, module search, module toggles, save, and close controls.
- Module lifecycle with isolated tick-error handling.
- Toggle Sprint and Toggle Sneak using vanilla input.
- Fullbright using local gamma, restoring the previous value when disabled.
- Zoom using local FOV, restoring the previous value when disabled.
- Local JSON persistence at `config/clownclient.json`.

## Not implemented yet

HUD editor/rendering, notifications, waypoints, profile import/export, advanced settings, combat-practice overlays, and diagnostics are not included. They are intentionally not shown as working modules.

## Build

Requires JDK 21 and Gradle:

```sh
gradle build
```

The remapped mod JAR should appear in `build/libs/`. To launch the development client:

```sh
gradle runClient
```

## Install

1. Install Fabric Loader for Minecraft 1.21.11.
2. Build the project.
3. Copy `build/libs/clown-client-0.1.0.jar` into the instance's `mods` folder.
4. Install the matching Fabric API release.
5. Launch the Fabric 1.21.11 profile and press **Right Shift**.

## Safety and privacy

No telemetry, credential collection, remote access, hidden downloads, anti-cheat evasion, packet manipulation, or automated attacks are included. Configuration is stored locally. Do not use gameplay-altering features on servers unless the owner and players have explicitly agreed.

## Verification status

The source tree and metadata are committed, but a Gradle build and in-game launch have **not** been verified. Treat this as an early source foundation, not a tested release.