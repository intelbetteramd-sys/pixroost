; Spike S-07: Inno Setup script. Packs the Compose app image into a per-machine .exe installer.
; Defines come from build-exe.ps1: AppImage, OutputDir, Version. Keep this file UTF-8 with BOM (Russian text).

[Setup]
AppId={{B5A3B8D2-6C0E-4A52-9C61-7F5E1D4B2A07}
AppName=Pixroost
AppVersion={#Version}
AppPublisher=Pixroost
DefaultDirName={autopf}\Pixroost
DefaultGroupName=Pixroost
PrivilegesRequired=admin
ArchitecturesAllowed=x64compatible
ArchitecturesInstallIn64BitMode=x64compatible
OutputDir={#OutputDir}
OutputBaseFilename=Pixroost-{#Version}-setup
Compression=lzma2
SolidCompression=yes
UninstallDisplayIcon={app}\Pixroost.exe
; The JVM launcher cannot hold a named mutex (AppMutex) and a tray app has no window to close, so the running
; app is killed explicitly (see [Code]); Restart Manager is off to avoid its "files in use" dialog.
CloseApplications=no
WizardStyle=modern

[Languages]
Name: "russian"; MessagesFile: "compiler:Languages\Russian.isl"
Name: "english"; MessagesFile: "compiler:Default.isl"

[Tasks]
Name: "desktopicon"; Description: "{cm:CreateDesktopIcon}"; GroupDescription: "{cm:AdditionalIcons}"
Name: "autostart"; Description: "Запускать при входе в Windows / Start at Windows sign-in"; Flags: unchecked

[Files]
Source: "{#AppImage}\*"; DestDir: "{app}"; Flags: recursesubdirs createallsubdirs ignoreversion

[Icons]
Name: "{group}\Pixroost"; Filename: "{app}\Pixroost.exe"
Name: "{autodesktop}\Pixroost"; Filename: "{app}\Pixroost.exe"; Tasks: desktopicon

[Registry]
Root: HKCU; Subkey: "Software\Microsoft\Windows\CurrentVersion\Run"; ValueType: string; ValueName: "Pixroost"; ValueData: """{app}\Pixroost.exe"""; Flags: uninsdeletevalue; Tasks: autostart

[Run]
; Inbound rule for the app on every network profile (domain, private, public).
Filename: "{sys}\netsh.exe"; Parameters: "advfirewall firewall add rule name=""Pixroost"" dir=in action=allow program=""{app}\Pixroost.exe"" profile=any enable=yes"; Flags: runhidden; StatusMsg: "Firewall rule..."
Filename: "{app}\Pixroost.exe"; Description: "{cm:LaunchProgram,Pixroost}"; Flags: nowait postinstall skipifsilent

[UninstallRun]
Filename: "{sys}\netsh.exe"; Parameters: "advfirewall firewall delete rule name=""Pixroost"""; Flags: runhidden; RunOnceId: "PixroostFirewallRemove"

[Code]
procedure StopRunningApp;
var
  ResultCode: Integer;
begin
  Exec(ExpandConstant('{sys}\taskkill.exe'), '/IM Pixroost.exe /F', '', SW_HIDE, ewWaitUntilTerminated, ResultCode);
end;

// Install or upgrade over a running app.
function PrepareToInstall(var NeedsRestart: Boolean): String;
begin
  StopRunningApp;
  Result := '';
end;

// Uninstall while the app sits in the tray.
function InitializeUninstall(): Boolean;
begin
  StopRunningApp;
  Result := True;
end;
