# Security and evaluation scope

This is a demonstration, not an operational security boundary for customer systems. Use fictional local records. There are no tenant accounts, MFA, RBAC, encrypted application database or tamper-evident audit logs.

The Android bridge is exposed only to packaged HTTPS content. Remote WebView navigation, file access and content access are blocked. The native AIS request has a fixed HTTPS URL, bounded response size, timeouts and no redirect following. The provider key stays in the backend environment. Local exports are user-directed and backup restoration validates schema before replacement.

The AIS gateway provides read-only public regional traffic with bounded storage and request rates. It does not accept customer uploads or write commands to ships, devices or networks. It is not a navigation system.

Report security concerns privately to centinellsuite@gmail.com. Never include passwords, private upload keys, provider keys or customer data in a public issue.
