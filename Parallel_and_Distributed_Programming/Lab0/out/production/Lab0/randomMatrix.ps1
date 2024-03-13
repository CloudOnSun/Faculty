$NO_ROWS = $args[0]
$NO_COLUMNS = $args[1]
$OUTPUT_FILE = $args[2]
$UPPER_LIMIT = $args[3]
 
function random_array {
    param($NO_ELEMENTS)
    $result = @()
    for ($i = 1; $i -le $NO_ELEMENTS; $i++) {
        $result += (Get-Random -Minimum 0 -Maximum $UPPER_LIMIT)
    }
    return $result
}
 
while ($NO_ROWS -gt 0) {
    $res = random_array $NO_COLUMNS
    $res | Out-File -Append $OUTPUT_FILE
    $NO_ROWS = $NO_ROWS - 1
}