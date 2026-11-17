# Compatibility notes — CLOWN CLIENT

- Target is pinned to Minecraft `1.21.11`; do not silently retarget.
- Fabric 1.21.11 release guidance identifies Fabric Loader `0.18.1` as a stable baseline and recommends Loom 1.14.
- The configured Fabric API is `0.141.1+1.21.11`.
- The project uses official Mojang mappings and Java 21.
- Loom is configured as `1.14-SNAPSHOT`; remote artifact resolution has not been verified in this environment.
- Recheck the official Fabric version selector before setting up a fresh development machine.