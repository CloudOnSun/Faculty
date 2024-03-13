
$param1 = $args[0] # Nume fisier cpp
#Write-Host $param1

$param3 = $args[1] # No of runs
#Write-Host $param3

$param4 = $args[2] # Input file name
#Write-Host $param4

$param5 = $args[3] # Type of program: 0 - sequential, 1 - parallel
#Write-Host $param5

$lini = $args[4] # N

$coloane = $args[5] # M

# Executare exec cpp

$suma = 0

for ($i = 0; $i -lt $param3; $i++){
    Write-Host "Rulare" ($i+1)
    $cppArguments = @($args[2], $args[3], $args[4], $args[5])
    $a = (cmd /c .\$($args[0]) $cppArguments) # rulare exec cpp
    
    Write-Host $a
    $suma += $a

    if ($param5 -eq 1) {
        $fileA = "output1.txt"
        $fileB = "output2.txt"
        if(Compare-Object -ReferenceObject $(Get-Content $fileA) -DifferenceObject $(Get-Content $fileB))
            {"files are different"}
        Else {"files are the same"}
    }

    Write-Host ""
}
$media = $suma / $i
#Write-Host $suma
Write-Host "Timp de executie mediu:" $media

# Creare fisier .csv
if (!(Test-Path outJ.csv)){
    New-Item outJ.csv -ItemType File
    #Scrie date in csv
    Set-Content outJ.csv 'Tip Program;Timp executie;No of Runs;FisierIntrare'
}

# Append
Add-Content outJ.csv "$($args[3]);$($media);$($args[1]);$($args[2])"