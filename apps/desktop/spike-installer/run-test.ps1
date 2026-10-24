# Spike S-07, runs inside Windows Sandbox: installs the .msi silently, shows the firewall rule, starts the app.
$ErrorActionPreference = 'Continue'
$msi = Get-ChildItem C:\Test -Filter 'Pixroost*.msi' | Select-Object -First 1
if (-not $msi) { Write-Host 'No Pixroost*.msi in C:\Test'; return }

Write-Host "Installing $($msi.Name) silently..."
$install = Start-Process msiexec.exe -ArgumentList "/i `"$($msi.FullName)`" /qn /l*v C:\Users\Public\pixroost-install.log" -Wait -PassThru
Write-Host "msiexec exit code: $($install.ExitCode) (0 = ok, log: C:\Users\Public\pixroost-install.log)"

Write-Host "`n--- Get-NetFirewallRule -DisplayName Pixroost* ---"
Get-NetFirewallRule -DisplayName 'Pixroost*' | Format-Table DisplayName, Enabled, Direction, Action, Profile -AutoSize
Get-NetFirewallRule -DisplayName 'Pixroost*' | Get-NetFirewallApplicationFilter | Format-Table Program -AutoSize

$exe = Join-Path $env:ProgramFiles 'Pixroost\Pixroost.exe'
Write-Host "Launching $exe"
if (Test-Path $exe) { Start-Process $exe } else { Write-Host 'Pixroost.exe not found' }

Write-Host "`nTo test uninstall and rule removal, run:"
Write-Host "  msiexec /x `"$($msi.FullName)`" /qn ; Get-NetFirewallRule -DisplayName 'Pixroost*'"
