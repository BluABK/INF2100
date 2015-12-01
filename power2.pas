program Power2;

var v: integer;


function pow2 (x: integer): integer;
var p2: integer;
begin
    p2 := 1;
    while 2*p2 <= x do p2 := 2*p2;
    pow2 := p2
end;

begin
    v := pow2(1000);
    write('pow2(1000) = ', v, eol)
end.
