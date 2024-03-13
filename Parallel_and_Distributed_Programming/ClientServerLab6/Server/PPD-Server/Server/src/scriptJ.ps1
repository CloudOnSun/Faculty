
$param1 = $args[0] # Nume fisier java
#Write-Host $param1

$param2 = $args[1] # No of runs
#Write-Host $param3

$param3 = $args[2] # Producers
#Write-Host $param4

$param4 = $args[3] # Consumers
#Write-Host $param4

$param5 = $args[4] # Delta T
#Write-Host $param5

# Executare class Java

$suma = 0

for ($i = 0; $i -lt $param2; $i++){
    Write-Host "Rulare" ($i+1)
   
    java $args[0] $args[2] $args[3] $args[4]
    $suma += $a

    $fileA = "output1.txt"
    $fileB = "output2.txt"
    if(Compare-Object -ReferenceObject $(Get-Content $fileA) -DifferenceObject $(Get-Content $fileB))
        {"files are different"}
    Else {"files are the same"}

    Write-Host ""
}
$media = $suma / $i
#Write-Host $suma
Write-Host "Timp de executie mediu:" $media

# Creare fisier .csv
if (!(Test-Path outJ.csv)){
    New-Item outJ.csv -ItemType File
    #Scrie date in csv
    Set-Content outJ.csv 'No of Runs;Timp Mediu;Nr producers;Nr Consumers;DeltaT'
}

# Append
Add-Content outJ.csv "$($args[1]);$($media);$($args[2]);$($args[3]);$($args[4])"