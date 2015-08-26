Program Fib;

var n: Integer;

function fib1(x: Integer): Integer;
var f1: Integer; f2: Integer; f3: Integer; i: Integer;
begin
	f1 := 0; f2 := 1;
	i := x;
	while i > 0 do begin
		f3 := f1+f2;

		f1 := f2;
		f2 := f3;

		i := i-1;
	end;
	fib1 := f1;
end; {fib1}

function fib2(x: Integer): Integer;
begin
	if x <= 2 then
		fib2 := 1
	else
		fib2 := fib2(x-2) + fib2(x-1);
end; {fib2}

{function main: }

begin
	n := 40;
	write('fib1(', n, ') =', fib1(n), eol);
	write('fib2(', n, ') =', fib2(n), eol);
end.

