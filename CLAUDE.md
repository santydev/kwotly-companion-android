# KwotlyCompanion — Project Context for Claude Code

> Ce fichier est auto-chargé par toute session Claude Code lancée
> dans `E:\KwotlyCompanion`. Il porte tout le contexte du chantier.

## Mission

Native Android app **portfolio piece** pour Santy, en route pour
décrocher un poste **Android Developer - AI Trainer chez DataAnnotation**
($50-100/h remote). Construite en parallèle à ton "vrai" produit
Kwotly (Next.js SaaS pour artisans US), elle prouve à DataAnnotation
qu'il sait écrire du Kotlin idiomatique moderne.

## Profil Santy (lire en premier)

50 ans, dev senior **30+ ans d'XP backend/web** (cf. son repo Kwotly :
Next.js 13, Prisma, Stripe Connect, marketplace V2, Jarvis MCP servers).
**JAMAIS touché Kotlin/Android natif avant 2026-06-06.** Apprend vite.

→ Traite-le comme **peer learning new framework**, PAS comme junior.
Skip "let me explain what a class is in Kotlin". Compare en disant
"comme `useState` mais avec StateFlow". Sois terse.

→ Lis aussi : `C:\Users\santy\.claude\projects\e--project\memory\MEMORY.md`
si tu veux le full profile (workflow multi-agent, brand rules Kwotly,
behavioral feedbacks). Pas obligatoire pour code Android pur.

## Stack locked-in

| Couche | Choix | Version |
|---|---|---|
| Android Studio | Quail | 2026.1.1 Patch 2 |
| AGP | Android Gradle Plugin | 9.2.1 |
| Kotlin | | 2.2.10 |
| KSP | | 2.2.10-2.0.2 (KSP2 line) |
| Compose BOM | | 2026.02.01 |
| minSDK | API 35 (Android 15 "VanillaIceCream") | |
| targetSDK | API 36 | |
| JDK | | 17 |

**Pas de débat sur les versions** — tout est lock par Santy après
discussion avec moi le 2026-06-06. Si Android Studio propose un bump,
demande à Santy d'abord.

## Libraries (8 + tests, full list dans `gradle/libs.versions.toml`)

- **Compose Material 3** (UI) + Material Icons Extended
- **Navigation Compose 2.8.5** (routing)
- **Hilt 2.52** (DI, via KSP)
- **Retrofit 2.11 + OkHttp 4.12 + Kotlinx Serialization 1.7.3** (HTTP + JSON)
- **DataStore Preferences 1.1.1** (JWT storage)
- **Lifecycle ViewModel/Runtime Compose 2.9.0** (state)
- **Coroutines 1.9.0**
- **Coil 3.0.4** (Compose-native image loader, pour quote photos plus tard)
- Tests : JUnit + Turbine 1.2 + MockK 1.13 + Compose UI Test

## Architecture cible (Clean Architecture light)

```
ai.kwotly.companion/
├── data/                   # Sources + persistence
│   ├── remote/             # Retrofit API + DTOs
│   ├── local/              # DataStore
│   └── repository/         # AuthRepository, QuotesRepository
├── domain/                 # Pure Kotlin, zero Android
│   ├── model/              # Quote, User, AuthSession
│   └── usecase/            # Optional — use repos directly si V1 reste simple
├── presentation/           # UI layer
│   ├── auth/               # Login screen + VM + UiState
│   ├── quotes/             # List + Detail screens
│   ├── camera/             # Camera capture pour photo upload
│   └── components/         # Reusable @Composables
├── di/                     # Hilt modules
├── theme/                  # Material 3 theme + WoW palette
├── KwotlyCompanionApp.kt   # @HiltAndroidApp Application
└── MainActivity.kt         # @AndroidEntryPoint, NavHost root
```

## Backend Kwotly (le vrai produit)

L'app talk à `https://app.kwotly.ai/` (production Railway).

**Endpoints utilisés** :
- `POST /api/auth/login` → JWT (Bearer token) + User
- `GET /api/quotes` → list quotes du user authentifié (Authorization: Bearer ...)
- `GET /api/quotes/[id]` → détail quote
- `POST /api/quotes/photo/upload` → upload photo, returns S3/R2 URL
- (FCM push à wirer en S2)

**Format JSON** : voir le repo Kwotly principal à `E:\project\` si besoin
de vérifier la shape exacte d'un payload (read-only — ne touche pas au repo Kwotly).

## Brand identity (à appliquer dans le theme + UI)

| Tier WoW | Hex | Usage |
|---|---|---|
| Rare | `#0070DD` (blue) | App primary, boutons d'action |
| Epic | `#9333EA` (purple) | Secondary, accents |
| Legendary | `#F0B82E` (gold) | Money moments (totaux, deposits, signed) |
| Common | `#A1A1AA` (zinc) | Neutral states |

**Theme : DARK MODE PAR DÉFAUT.** Light mode optional (Material You).
**Mascotte** : bean+casque jaune (dispo dans `~/grok-out/` ou via
Grok-generated). Pas urgent pour MVP.

**NO competitor names. NO personal/family/immigrant context dans la copy
publique.** Santy est cofondateur solo + 30 ans d'XP — ne pas mentionner
son histoire perso comme part du marketing.

## Plan exécution 3 semaines

### S1 (Foundation + Auth + Quotes core)

- **J0 ✅ DONE** (2026-06-06 soir) — project scaffold, emulator API 35, Hello Android, push GitHub
- **J1 EN COURS** — split en 4 chunks :
  - **Chunk 1 EN COURS** : libs.versions.toml + build.gradle.kts (deps + plugins + JDK 17 + BuildConfig)
    - Fixed 2x : KSP version `2.2.10-2.0.2`, removed duplicate `kotlin-android` plugin (déjà appliqué par `kotlin-compose`)
    - **À FAIRE** : verify Gradle Sync passe vert
  - **Chunk 2** : theme Material 3 WoW + package structure + Application class + AndroidManifest
  - **Chunk 3** : Login backend (KwotlyApi.kt Retrofit + AuthDataStore + AuthRepository + Hilt modules NetworkModule/DataStoreModule/RepositoryModule)
  - **Chunk 4** : Login UI (LoginViewModel + sealed UiState + LoginScreen + MainActivity NavHost)
- **J2-J3** : Quotes list + détail + camera capture
- **J4-J5** : Tests viewmodel (Turbine) + 1 Compose UI test + README pro EN

### S2 (Polish)

- FCM push notifications
- Dark mode + Material You dynamic theming
- Animations + edge-to-edge handling
- Code review pass (dedup, sealed errors, naming)

### S3 (Portfolio + Apply)

- README EN architecture decisions + screenshots emulator
- 2 articles techniques EN :
  1. "Race-safe quote claiming with Prisma updateMany" (basé sur lib/usage.ts du repo Kwotly principal)
  2. "Integrating Anthropic tool-use in production Next.js" (basé sur lib/admin-chat-tools du repo Kwotly principal)
- Resume EN polish + GitHub clean
- **Apply DataAnnotation** : [indeed.com/viewjob?jk=d3727428f95ae45a](https://www.indeed.com/viewjob?jk=d3727428f95ae45a)

## Conventions de code

- **Kotlin idiomatique 2026** — `val` par défaut, `data class`, sealed
  interfaces pour UiState, extension functions, scope functions (let,
  apply, run quand approprié).
- **Coroutines + Flow** (pas RxJava). `StateFlow` pour state UI,
  `SharedFlow` pour events one-shot.
- **Compose state hoisting** — children = `(state, onAction) -> UI`,
  state vit dans le ViewModel.
- **DI strict** — pas de `object` singletons custom, tout par Hilt.
- **Pas de `!!`** sauf cas vraiment justifié. Préfère `?:` ou
  `requireNotNull(...)` avec un message clair.
- **Pas de `runBlocking`** sauf dans des tests.
- **Pas de `GlobalScope`** — toujours `viewModelScope` ou `LaunchedEffect`.
- **KDoc en anglais** sur les classes/fonctions publiques. Pas de
  fluff — expliquer le WHY si non-évident, pas le WHAT.

## Règles de comportement (lire avant chaque turn)

1. **No baby steps.** Santy est senior. Compare aux concepts qu'il
   connaît déjà (React → Compose). Skip les bases.
2. **No blabla.** Réponse directe, pas de "Excellente question !"
   ou "Voici les options ci-dessous".
3. **Push back welcome.** Si Santy propose un truc sous-optimal, dis-le.
   Validate son intent, propose mieux, laisse-le choisir.
4. **Speak fr-FR + en mix.** Comme un poto. Tu peux passer EN pour
   les noms techniques et code. Pas de formality.
5. **Don't dump.** Chunks de ~50-150 lignes max. Validate avec
   Santy entre les chunks (sync passé ? compile vert ?).
6. **Verify versions on Maven Central** avant de proposer un lib
   ou plugin — j'ai déjà fait l'erreur KSP `2.2.10-1.0.27` qui
   n'existait pas. Le bon réflexe = curl `repo1.maven.org` ou
   demander à Santy de vérifier dans Android Studio.
7. **Stack locked.** Ne propose pas de switch vers d'autres
   architectures (MVI vs MVVM, Koin vs Hilt) — pas le moment.
   On ship d'abord, on raffine en S2/S3.

## Quick reference

- Repo : [github.com/santydev/kwotly-companion-android](https://github.com/santydev/kwotly-companion-android)
- Kwotly main repo (source de vérité pour API contracts) : `E:\project\`
- Emulator : Pixel 8 Pro API 35
- Santy email Railway/GitHub : santy.benaly@gmail.com (Railway: santy.stark.us@gmail.com)
