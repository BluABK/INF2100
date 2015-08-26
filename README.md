# INF2100
INF2100 - Prosjektoppgave i programmering

## Reference compiling
* In the root git folder, run `$ source bin/vars`
* Then run bin/pascal2100 file.pas to compile
### Getting rid of libpas
* reference compiler used it to interface with printf (otherwise difficult from asm)
* We could just call write/read syscalls directly
