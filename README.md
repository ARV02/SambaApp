# SambaApp

![Android](https://img.shields.io/badge/Platform-Android-green)
![Java](https://img.shields.io/badge/Legacy-Java-orange)
![Kotlin](https://img.shields.io/badge/Modernization-Kotlin-blue)
![Jetpack Compose](https://img.shields.io/badge/UI-Jetpack%20Compose-purple)
![Status](https://img.shields.io/badge/Status-In%20Modernization-yellow)

Legacy Android application built in Java in 2020 as a university project to connect Android devices to shared folders using SMB/Samba.

This project is currently being revived and modernized as part of my personal Android lab. The goal is to evolve an old Java/XML Android app into a modern Android project using Kotlin, Jetpack Compose, better architecture, and current Android development practices.

---

## Overview

**SambaApp** is an Android application designed to connect to shared folders through the SMB/Samba protocol.

The original version was created during university while I was starting my Android development journey. The app was tested with shared folders exposed from different Unix/Linux-based environments, including:

- FreeBSD
- Solaris
- Fedora

The first goal was simple:

> Make an Android app connect to shared folders using Samba/SMB.

Years later, the goal is different:

> Modernize a legacy Android application while preserving the original idea and improving its architecture, maintainability, and security.

---

## Personal note

This project has a special meaning for me.

I originally built it in 2020 as a university project. At that time, I was still learning Android development, and this was one of the first projects where I had to go beyond a typical mobile app.

I had to understand how Android could communicate with shared folders over the network, how SMB/Samba worked, how permissions were handled, and how different operating systems exposed shared resources.

Years later, I decided to bring it back, not because the original code was perfect, but because it represents an important part of my growth as an Android developer: learning by building, testing, breaking things, and improving one step at a time.

---

## Why revive this project?

This repository is now part of my personal Android lab.

The main purpose is to take a legacy Android project and modernize it progressively.

This project allows me to practice and demonstrate:

- Legacy Android modernization
- Java to Kotlin migration
- XML to Jetpack Compose migration
- SMB/Samba networking
- Clean Architecture principles
- MVVM
- Better state management
- Improved error handling
- Secure credential handling
- Gradual refactoring
- Documentation and maintainability

---

## Current status

The current codebase still reflects the original 2020 implementation.

It was built with:

- Java
- XML layouts
- Android Views
- Activities and Fragments
- SMB/Samba connection logic

The modernization process is planned to be gradual. The first goal is to stabilize the original project, document how it works, and then start migrating the codebase step by step.

---

## Existing features

The original version includes:

- SMB/Samba connection from Android
- Shared folder browsing
- Basic file listing
- Separate flows for different operating systems
- Connection testing with:
  - FreeBSD
  - Solaris
  - Fedora

---

## Modernization goals

The new version will focus on:

- Migrating Java code to Kotlin
- Replacing XML layouts with Jetpack Compose
- Removing duplicated SMB connection logic
- Creating reusable connection profiles
- Improving error handling
- Improving loading and connection states
- Using coroutines instead of legacy async patterns
- Separating UI, domain, and data layers
- Adding secure credential storage
- Improving documentation
- Adding tests
- Adding CI/CD with GitHub Actions

---

## Roadmap

This roadmap tracks the modernization process of SambaApp, from a legacy Java/XML Android project to a modern Android application using Kotlin, Jetpack Compose, better architecture, and safer SMB/Samba handling.

---

### v0.1.0 - Repository revival

- [x] Update Gradle Wrapper

- [x] Update Android Gradle Plugin

- [x] Replace deprecated repositories with Maven Central

- [x] Remove sensitive logs

- [x] Add project README

- [x] Add `.gitignore`

- [x] Verify the project compiles successfully

---

### v0.2.0 - Legacy cleanup

- [x] Add Kotlin support

- [x] Add centralized Bundle key constants

- [x] Clean unused imports

- [x] Format legacy Java, Kotlin, and Gradle files

- [x] Review AndroidManifest permissions

- [x] Review deprecated or unused dependencies

- [x] Remove unused legacy `jcifs` dependency/files if no longer required

- [x] Document the current SMB/Samba flow

- [x] Identify duplicated logic between FreeBSD, Solaris, and Fedora flows

- [x] Mark hardcoded connection values for future refactoring

---

### v0.3.0 - Configurable SMB connections

- [x] Remove hardcoded IP addresses

- [x] Remove hardcoded share names

- [x] Add a connection profile model

- [x] Allow users to enter host, share name, username, and password

- [x] Add basic field validation

- [x] Keep optional presets for FreeBSD, Solaris, and Fedora

---

### v0.4.0 - SMB logic refactor

- [x] Move SMB/Samba connection logic out of Activities and Fragments

- [x] Create a reusable SMB repository

- [x] Unify file listing logic

- [x] Reduce duplicated code between operating system flows

- [x] Improve connection error handling

---

### v0.5.0 - Async modernization

- [x] Replace `AsyncTask` with coroutines

- [x] Run network operations on `Dispatchers.IO`

- [x] Add cancellable SMB operations

- [x] Improve loading, success, and error states

- [x] Prepare async flow for MVVM

---

### v0.6.0 - MVVM architecture

- [ ] Add ViewModels

- [ ] Move UI state out of Fragments

- [ ] Expose state using StateFlow or LiveData

- [ ] Separate UI logic from business logic

- [ ] Keep legacy XML UI working during the transition

- [ ] Prepare the project for Compose migration

---

### v0.7.0 - Jetpack Compose foundation

- [ ] Add Jetpack Compose

- [ ] Add Material 3

- [ ] Create the app theme and color system

- [ ] Create reusable Compose components

- [ ] Keep interoperability with the legacy Fragment/XML flow

---

### v0.8.0 - New connection flow in Compose

- [ ] Create a Compose-based connection screen

- [ ] Add profile name, host, share name, username, and password fields

- [ ] Add preset chips for FreeBSD, Solaris, Fedora, and Custom

- [ ] Add loading and error states

- [ ] Connect the screen with the ViewModel

---

### v0.9.0 - Modern SMB file browser

- [ ] Create a Compose-based file browser

- [ ] Navigate through remote folders

- [ ] Display files and folders with clear UI states

- [ ] Refresh file lists

- [ ] Show empty and error states

- [ ] Add screenshots and demo GIF to the README

---

### v1.0.0 - Stable modern demo

- [ ] Complete the main Compose flow

- [ ] Support configurable SMB connections

- [ ] Support remote file listing

- [ ] Add connection profiles UI

- [ ] Add basic settings screen

- [ ] Add screenshots and demo GIF

- [ ] Create a stable demo release

---

### v1.1.0 - Remote file operations

- [ ] Create remote folders

- [ ] Create remote files

- [ ] Delete remote files

- [ ] Delete empty folders

- [ ] Add confirmation dialogs before destructive actions

- [ ] Refresh the file list after each operation

---

### v1.2.0 - File transfers

- [ ] Upload local files to SMB shared folders

- [ ] Download remote files to the Android device

- [ ] Show transfer progress

- [ ] Support background transfers with WorkManager

- [ ] Handle large files safely

---

### v1.3.0 - Security improvements

- [ ] Add secure credential storage

- [ ] Avoid storing plain text passwords

- [ ] Clear passwords from memory when possible

- [ ] Add connection profile validation

- [ ] Add optional read-only mode

---

### v1.4.0 - Project quality

- [ ] Add unit tests

- [ ] Add GitHub Actions

- [ ] Add static analysis

- [ ] Add lint checks

- [ ] Improve README documentation

- [ ] Add architecture diagram

---

### v1.5.0 - Portfolio polish

- [ ] Add screenshots

- [ ] Add demo video or GIF

- [ ] Add Before vs After section

- [ ] Add release notes

- [ ] Add GitHub topics

- [ ] Prepare the repository as a featured GitHub project

---

### v2.0.0 - Play Store candidate

- [ ] Update the app package name

- [ ] Update target SDK to meet Google Play requirements

- [ ] Generate a signed Android App Bundle

- [ ] Prepare Play Store listing

- [ ] Add production-ready screenshots

- [ ] Add privacy policy

- [ ] Complete Data Safety form

- [ ] Create internal testing release

- [ ] Fix Play Console warnings

- [ ] Publish first production release

## Tech stack

### Original stack

- Java
- Android SDK
- XML layouts
- AppCompat
- Fragments
- SMB/Samba libraries

### Target stack

- Kotlin
- Jetpack Compose
- Material 3
- ViewModel
- StateFlow
- Coroutines
- Navigation Compose
- Hilt
- DataStore or EncryptedSharedPreferences
- WorkManager
- Clean Architecture / MVVM
- GitHub Actions

---

## Architecture target

The modernization process aims to separate responsibilities into clear layers:

```text
app
├── presentation
│   ├── screens
│   ├── components
│   ├── navigation
│   └── viewmodels
│
├── domain
│   ├── model
│   ├── repository
│   └── usecase
│
└── data
    ├── samba
    ├── repository
    ├── mapper
    └── local
