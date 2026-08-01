# 📱 CitizenReport - Application de Signalement Citoyen

**CitizenReport** est une application mobile Android native développée avec **Kotlin** et **Jetpack Compose**. Elle permet aux citoyens de signaler facilement des incidents urbains (voirie, éclairage, propreté) et offre aux autorités une interface d'administration pour traiter ces signalements et améliorer la gestion de la ville.

---

## 🚀 Fonctionnalités Principales

* 🔐 **Gestion des Accès :** Authentification (Espace Citoyen & Espace Administrateur).
* 📢 **Signalement d'Incidents :** Soumission rapide d'un problème avec titre, description, catégorie, niveau de priorité et photo.
* 🤖 **Attribution Automatique :** Suggestion automatique du service compétent selon la catégorie (ex: *Service Voirie*, *Gestion des Déchets*).
* 👍 **Engagement Citoyen :** Système de vote pour appuyer les signalements prioritaires et attribution d'un statut citoyen (ex: *Citoyen d'Argent*).
* ⚙️ **Panneau d'Administration :** Modification en temps réel de l'état des signalements (*Reçu*, *En cours*, *Résolu*).
* 🗄️ **Persistance des Données :** Sauvegarde locale intégrée avec **Room Database**.

---

## 🛠️ Technologies & Architecture

* **Langage :** Kotlin
* **Interface Graphique :** Jetpack Compose, Material Design 3
* **Architecture :** MVVM (Model-View-ViewModel)
* **Base de données locale :** Room Database (StateFlow & Coroutines)
* **Version Android Minimale :** Android 8.0 (API level 26+)

---

## 📦 Installation & Exécution

1. **Cloner ou télécharger le projet :** Téléchargez les fichiers de ce dépôt GitHub.
2. **Ouvrir dans Android Studio :** 
   * Lancez Android Studio.
   * Cliquez sur **Open** et sélectionnez le dossier `CitizenReport`.
3. **Lancer l'application :** Attendez la fin de la synchronisation Gradle, puis exécutez l'application sur un émulateur ou un appareil Android physique.

---

## 👤 Auteur & Projet

* **Développé par :** Nogaye Mbodj (`@nogayembodj2`)
* **Établissement :** UNCHK
* **Projet :** CitizenReport Pro (Android Native)
