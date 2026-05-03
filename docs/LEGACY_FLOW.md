# Legacy SMB/Samba Flow

## Current behavior

The original version of SambaApp has separate flows for each operating system:

- Fedora
- FreeBSD
- Solaris

Each flow uses a dedicated Activity and Fragment to connect to a specific SMB/Samba shared folder.

## Current connection flow

1. The user selects an operating system from the main screen.
2. The app opens the selected Activity.
3. The user enters credentials.
4. The credentials are passed through a Bundle.
5. The Fragment creates an SMB client.
6. The app connects to a hardcoded host.
7. The app authenticates using the entered credentials.
8. The app connects to a hardcoded share.
9. The app lists remote files.
10. The file names are displayed in a ListView.

## Current limitations

- Hosts are hardcoded.
- Share names are hardcoded.
- SMB logic is duplicated between operating system flows.
- UI and SMB connection logic are coupled.
- AsyncTask is used for background operations.
- Error handling is basic.
- Runtime testing requires recreating the original VM environment or using a local Samba test server.

## Duplicated logic identified

The FreeBSD, Solaris, and Fedora file listing flows share similar responsibilities:

- Read credentials from a Bundle.
- Create an SMB client.
- Connect to a server.
- Authenticate the SMB session.
- Connect to a share.
- List remote files.
- Map remote files to list items.
- Display results in a ListView.

This duplicated logic will be moved into a reusable SMB repository in a future refactor.