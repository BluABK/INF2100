# Code file created by Pascal2100 compiler 2015-08-26 10:48:30
        .extern write_char     
        .extern write_int      
        .extern write_string   
        .globl  _main          
        .globl  main           
_main:                                  
main:   call    prog$p2_1               # Start program
        movl    $0,%eax                 # Set status 0 and
        ret                             # terminate the program
func$f_2:
        enter   $40,$2                  # Start of f
        movl    $1,%eax                 #   1
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        movl    0(%edx,%eax,4),%eax     #   a[...]
        movl    -8(%ebp),%edx           
        movl    %eax,-44(%edx)          # x :=
        movl    $2,%eax                 #   2
        movl    -8(%ebp),%edx           
        movl    %eax,-40(%edx)          # i :=
.L0003:                                 # Start while-statement
        movl    -8(%ebp),%edx           
        movl    -40(%edx),%eax          #   i
        pushl   %eax                    
        movl    $6,%eax                 #   6
        popl    %ecx                    
        cmpl    %eax,%ecx               
        movl    $0,%eax                 
        setle   %al                     # Test <=
        cmpl    $0,%eax                 
        je      .L0004                  
                                        # Start if-statement
        movl    -8(%ebp),%edx           
        movl    -40(%edx),%eax          #   i
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        movl    0(%edx,%eax,4),%eax     #   a[...]
        pushl   %eax                    
        movl    -8(%ebp),%edx           
        movl    -44(%edx),%eax          #   x
        popl    %ecx                    
        cmpl    %eax,%ecx               
        movl    $0,%eax                 
        setg    %al                     # Test >
        cmpl    $0,%eax                 
        je      .L0005                  
        movl    -8(%ebp),%edx           
        movl    -40(%edx),%eax          #   i
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        movl    0(%edx,%eax,4),%eax     #   a[...]
        movl    -8(%ebp),%edx           
        movl    %eax,-44(%edx)          # x :=
.L0005:                                 # End if-statement
        movl    -8(%ebp),%edx           
        movl    -40(%edx),%eax          #   i
        pushl   %eax                    
        movl    $1,%eax                 #   1
        movl    %eax,%ecx               
        popl    %eax                    
        addl    %ecx,%eax               #   +
        movl    -8(%ebp),%edx           
        movl    %eax,-40(%edx)          # i :=
        jmp     .L0003                  
.L0004:                                 # End while-statement
        movl    -8(%ebp),%edx           
        movl    -44(%edx),%eax          #   x
        movl    %eax,-32(%ebp)          # f :=
        movl    -32(%ebp),%eax          # Fetch return value
        leave                           # End of f
        ret                             
prog$p2_1:
        enter   $56,$1                  # Start of p2
        movl    $17,%eax                #   17
        pushl   %eax                    
        movl    $1,%eax                 #   1
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        popl    %ecx                    
        movl    %ecx,0(%edx,%eax,4)     # a[x] :=
        movl    $2,%eax                 #   2
        negl    %eax                    #   - (prefix)
        pushl   %eax                    
        movl    $2,%eax                 #   2
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        popl    %ecx                    
        movl    %ecx,0(%edx,%eax,4)     # a[x] :=
        movl    $22,%eax                #   22
        pushl   %eax                    
        movl    $3,%eax                 #   3
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        popl    %ecx                    
        movl    %ecx,0(%edx,%eax,4)     # a[x] :=
        movl    $12,%eax                #   12
        pushl   %eax                    
        movl    $4,%eax                 #   4
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        popl    %ecx                    
        movl    %ecx,0(%edx,%eax,4)     # a[x] :=
        movl    $0,%eax                 #   0
        pushl   %eax                    
        movl    $5,%eax                 #   5
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        popl    %ecx                    
        movl    %ecx,0(%edx,%eax,4)     # a[x] :=
        movl    $9,%eax                 #   9
        pushl   %eax                    
        movl    $6,%eax                 #   6
        subl    $1,%eax                 
        movl    -4(%ebp),%edx           
        leal    -56(%edx),%edx          
        popl    %ecx                    
        movl    %ecx,0(%edx,%eax,4)     # a[x] :=
        call    func$f_2                #   f
        pushl   %eax                    # Push param #1.
        call    write_int               
        addl    $4,%esp                 # Pop parameter.
        movl    $10,%eax                #   char 10
        pushl   %eax                    # Push param #2.
        call    write_char              
        addl    $4,%esp                 # Pop parameter.
        leave                           # End of p2
        ret                             
