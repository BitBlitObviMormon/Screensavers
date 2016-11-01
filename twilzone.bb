;--------------------------------------------------------------------------
;                                                                rno 010800
;                                                         noospot42@f2s.com
;
; don't ask me what it does, I took the formula from under a nice picture 
; in a book (pictures of chaos something) and made it on amiga blitz but 
; ages are needed to get one frame on my amiga, I was curious of the result
; with litle more cpu/fpu power... :)
; use debug off 
; BlitzDemo 1.1
;--------------------------------------------------------------------------

Graphics 640,480

Text 0,0,"Welcome to the twilight zone :)"
Text 0,36,"for each 'seed' point of the grid ,"
Text 0,48,"         do this n time  ( n=50 ) :"    ; n is itr
Text 0,72,"   f(x) = xold - t * ( Sin( xold + Sin( 3*yold ) ) ) "
Text 0,84,"   f(y) = yold + t * ( Sin( yold + sin( 3*xold ) ) ) "
Text 0,108," [   ESC ]     quit"
Text 0,120," [    Up ]     zoom"
Text 0,132," [  Down ]     unzoom"
Text 0,144," [  Left ]     increase t"
Text 0,156," [ Right ]     decrease t"
Text 0,168," [ Pg Up ]     increase number of seeds"
Text 0,180," [ Pg Dn ]     decrease number of seeds"
Text 0,204," press a key..."
Flip
WaitKey()

;--------------------------------------------------------------------------
Global dwidth = GadgetWidth(Desktop())              ;display width
Global dheight= GadgetHeight(Desktop())              ;display height
    Const nbc = 256               ;number of values for rgb
  Global drs% = dheight/2         ;max square resolution display 
         res# = 10                ;seeds points number (per row)
   Const itr% = 50                ;iterations maximum (length of branches)
    Const xc% = 0                 ;center x value
    Const yc% = 0                 ;center y value
          ti# = 8                 ;(bnd*2)/res     ;increment constant (seeds points)
           z# = 8                 ;z will pass boundaries values (zoom)
           t# =.1                 ;function factor
Const deg_fac#=180/Pi             ;used by deg function
Const rad_fac#=Pi/180             ;used by rad function
     refresh% = 1                 ;wether screen refresh is needed (1) or not (0)
Text 0,0, " Go!!"
;-Main---------------------------------------------------------------------
Graphics dwidth,dheight           ;enter graphic mode  
SetBuffer BackBuffer()            ;use 'no hassle' double buffering :)     
Origin dwidth/2,dheight/2         ;set the 0,0 point for drawing to middle of screen

 While Not KeyDown(1)             ;While not [ESC]

    If KeyDown(200)               ;if up arrow
      z = .98*z                   ;zoom 
      refresh=1                   ;need refresh
    EndIf
    If KeyDown(208)               ;if down arrow
      z = z/.98                   ;unzoom
       refresh=1
    EndIf
    If KeyDown(203)               ;if left arrow
      t = t+.01                   ;increase t 
      refresh=1
    EndIf
    If KeyDown(205)               ;if right arrow
      t = t-.01                   ;decrease t
      refresh=1
    EndIf
    If KeyDown(201)               ;if page up
      res = res+1                 ;increase res 
      refresh=1
    EndIf
    If KeyDown(209)               ;if page down
      res = res-1                 ;decrease res
      If res<=1 Then res=1        ;to avoid division by zero in frame function
      refresh=1
    EndIf

    If refresh=1                  ;check if any refresh is needed
      Cls                         ;delete curent buffer
      refresh = 0                      
      doit(z,t,res)               ;calc a frame
      Text 0,0,t#
      Flip                        ;flip drawbuffer
    EndIf
 Wend
EndGraphics                       ;quit graphic mode
End                               ;bye
;--------------------------------------------------------------------------
Function doit(bnd#,t#,res#)
 Local i%                         ;iteration counter 
 Local xb# = bnd                  ;x boundaries
 Local yb# = bnd                  ;y boundaries
 Local df# = drs/bnd              ;display factor  
 Local xi# = xc-xb                ;initial value of x (upper left seed)
 Local yi# = yc-yb                ;initial value of y
 Local  x# = 0                    ;maths' x, 
 Local  y# = 0                    ;         y 
 Local sx# = xi                   ;current seed
 Local sy# = yi                   ;            x,y  
 Local xo# = 0 : yo# = 0          ;allow reference to former iteration x or y value
 Local ti# = (bnd*2)/res          ;seeds incrementation
  While sy <= yb
     While sx <= xb                ;seeds forms a grid 
        x = sx : y = sy           ;current seed 
        For i=0 To itr            ;
          xo = x : yo = y         ;remember former value
;-----------------                ;if you want to experiment with different pics try changing this
          x = xo-t*Sin(deg(Sin(deg(3*yo))+xo)) ;new x value 
          y = yo+t*Sin(deg(Sin(deg(3*xo))+yo)) ;New y value
;-----------------
          Color (i*i) Mod nbc,(Abs(sx)*i) Mod nbc,(Abs(sy)*i) Mod nbc   ;calculate some color :)
;          Rect df*x,df*y,1,1      ;draw a point (fast)
         Plot df*x,df*y          ;draw a point 
        Next
        sx=sx+ti                  ;next seed point 
     Wend
     sy=sy+ti                     ;next seed row
     sx=xi                        ;beginning of row
   Wend 
End Function
;--------------------------------------------------------------------------
Function rad#(deg_ang#)           ;convert degrees to radians
  deg_ang=deg_ang*rad_fac         ;Const rad_fac#=Pi/180
 Return deg_ang
End Function
;--------------------------------------------------------------------------
Function deg#(rad_ang#)           ;convert radians to degrees 
  rad_ang=rad_ang*deg_fac         ;Const deg_fac#=180/Pi
 Return rad_ang
End Function
;--------------------------------------------------------------------------