# Code file created by Pascal2100 compiler 2015-12-01 14:19:20
        .extern write_char
        .extern write_int
        .extern write_string
        .globl  main
        .globl  _main
_main:
main:
        call    prog$fib_1
        movl    $0,%eax
        ret
func$fib1_2:                            # fib1 (level 2)
        enter   $48,$2
        movl    $0,%eax
        movl    -8(%ebp),%edx
        movl    %eax,-36(%edx)          # f1 := %eax
        movl    $1,%eax
        movl    -8(%ebp),%edx
        movl    %eax,-40(%edx)          # f2 := %eax
        movl    -8(%ebp),%edx
        movl    8(%edx),%eax
        movl    -8(%ebp),%edx
        movl    %eax,-48(%edx)          # i := %eax
.L0003:                                 # while .L0003
        movl    -8(%ebp),%edx
        movl    -48(%edx),%eax
        push    %eax
        movl    $0,%eax
        pop     %ecx
        cmpl    %eax,%ecx               # >
        setg    %al                     # ^
        movzbl  %al,%eax                # ^
        cmpl    $0,%eax
        je      .L0004
        movl    -8(%ebp),%edx
        movl    -36(%edx),%eax
        push    %eax
        movl    -8(%ebp),%edx
        movl    -40(%edx),%eax
        pop     %ecx
        addl    %ecx,%eax
        movl    -8(%ebp),%edx
        movl    %eax,-44(%edx)          # f3 := %eax
        movl    -8(%ebp),%edx
        movl    -40(%edx),%eax
        movl    -8(%ebp),%edx
        movl    %eax,-36(%edx)          # f1 := %eax
        movl    -8(%ebp),%edx
        movl    -44(%edx),%eax
        movl    -8(%ebp),%edx
        movl    %eax,-40(%edx)          # f2 := %eax
        movl    -8(%ebp),%edx
        movl    -48(%edx),%eax
        push    %eax
        movl    $1,%eax
        pop     %ecx
        subl    %ecx,%eax
        movl    -8(%ebp),%edx
        movl    %eax,-48(%edx)          # i := %eax
        jmp     .L0003
.L0004:                                 # /while .L0003
        movl    -8(%ebp),%edx
        movl    -36(%edx),%eax
        movl    -8(%ebp),%edx
        movl    %eax,-32(%edx)          # fib1 := %eax
        movl    -32(%ebp),%eax
        leave
        ret
func$fib2_5:                            # fib2 (level 2)
        enter   $32,$2
        movl    -8(%ebp),%edx
        movl    8(%edx),%eax
        push    %eax
        movl    $2,%eax
        pop     %ecx
        cmpl    %eax,%ecx               # <=
        setle   %al                     # ^
        movzbl  %al,%eax                # ^
        cmpl    $0,%eax                 # if .L0006
        je      .L0006
        movl    $1,%eax
        movl    -8(%ebp),%edx
        movl    %eax,-32(%edx)          # fib2 := %eax
        jmp     .L0007
.L0006:
        movl    -8(%ebp),%edx
        movl    8(%edx),%eax
        push    %eax
        movl    $2,%eax
        pop     %ecx
        subl    %ecx,%eax
        push    %eax
        call    func$fib2_5
        addl    $16,%esp
        neg     %eax
        push    %eax
        movl    -8(%ebp),%edx
        movl    8(%edx),%eax
        push    %eax
        movl    $1,%eax
        pop     %ecx
        subl    %ecx,%eax
        push    %eax
        call    func$fib2_5
        addl    $16,%esp
        push    %eax
        movl    -8(%ebp),%edx
        movl    8(%edx),%eax
        push    %eax
        movl    $3,%eax
        pop     %ecx
        subl    %ecx,%eax
        push    %eax
        call    func$fib2_5
        addl    $16,%esp
        pop     %ecx
        imull   %ecx,%eax
        pop     %ecx
        addl    %ecx,%eax
        movl    -8(%ebp),%edx
        movl    %eax,-32(%edx)          # fib2 := %eax
.L0007:
        movl    -32(%ebp),%eax
        leave
        ret
proc$nom_8:                             # nom (level 2)
        enter   $32,$2
        leave
        ret
prog$fib_1:                             # fib (level 1)
        enter   $40,$1
        call    proc$nom_8
        call    proc$nom_8
        movl    $40,%eax
        movl    -4(%ebp),%edx
        movl    %eax,-36(%edx)          # n := %eax
        .data
.L0009:
.asciz   "fib1("
        .align  2
        .text
        leal    .L0009,%eax
        push    %eax
        call    write_string
        addl    $4,%esp
        movl    -4(%ebp),%edx
        movl    -36(%edx),%eax
        push    %eax
        call    write_int
        addl    $4,%esp
        .data
.L0010:
.asciz   ") = "
        .align  2
        .text
        leal    .L0010,%eax
        push    %eax
        call    write_string
        addl    $4,%esp
        movl    -4(%ebp),%edx
        movl    -36(%edx),%eax
        push    %eax
        call    func$fib1_2
        addl    $16,%esp
        push    %eax
        call    write_int
        addl    $4,%esp
        movl    $10,%eax
        push    %eax
        call    write_char
        addl    $4,%esp
        .data
.L0011:
.asciz   "fib2("
        .align  2
        .text
        leal    .L0011,%eax
        push    %eax
        call    write_string
        addl    $4,%esp
        movl    -4(%ebp),%edx
        movl    -36(%edx),%eax
        push    %eax
        call    write_int
        addl    $4,%esp
        .data
.L0012:
.asciz   ") = "
        .align  2
        .text
        leal    .L0012,%eax
        push    %eax
        call    write_string
        addl    $4,%esp
        movl    -4(%ebp),%edx
        movl    -36(%edx),%eax
        push    %eax
        call    func$fib2_5
        addl    $16,%esp
        push    %eax
        call    write_int
        addl    $4,%esp
        movl    $10,%eax
        push    %eax
        call    write_char
        addl    $4,%esp
        movl    $3,%eax
        movl    -4(%ebp),%edx
        movl    %eax,-40(%edx)          # iii := %eax
        movl    -32(%ebp),%eax
        leave
        ret
