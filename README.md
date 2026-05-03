# SambaApp

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
