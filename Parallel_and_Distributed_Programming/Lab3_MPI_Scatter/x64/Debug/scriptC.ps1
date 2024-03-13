
$param1 = $args[0] # Nume fisier cpp
#Write-Host $param1

$param2 = $args[1] # No of processes
#Write-Host $param2

$param3 = $args[2] # No of runs
#Write-Host $param3

$suma = 0

for ($i = 0; $i -lt $param3; $i++){
    Write-Host "Rulare" ($i+1)
    $a = (cmd /c mpiexec -n $param2 $param1) # rulare exec cpp
    
    Write-Host $a
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
    Set-Content outJ.csv 'Nr processes;Timp executie;No of Runs'
}

# Append
Add-Content outJ.csv "$($args[1]);$($media);$($args[2])"