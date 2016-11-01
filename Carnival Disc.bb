;Swirl demo (featuring Sin & cos)
;written by Ed Upton

If Trim$(CommandLine$()) <> "/S" Then End

Global w=GadgetWidth(Desktop())
Global h=GadgetHeight(Desktop())

Graphics w,h
SetBuffer BackBuffer()

Dim angle(w/2)
Dim r(w/2)
Dim g(w/2)
Dim b(w/2)

Setup()

t=0
x = MouseX()
y = MouseY()
While Not KeyDown(1)
 e = WaitEvent(1)
 If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
; Cls
 update()
 Flip
 t=t+1
 If t>320 
  t=0
  Setup()
 EndIf
Wend
End

Function Setup()
 For ca=0 To w/2-1
  angle(ca)=0
  r(ca)=Rnd(255)
  g(ca)=Rnd(255)
  b(ca)=Rnd(255)
 Next
End Function

Function update()
 For ca=0 To w/2-1
  For nn=angle(ca) To angle(ca)+360 Step 60
   Color r(ca),g(ca),b(ca)
   xx=w/2+Cos((2*((angle(ca)+nn)*182))*Pi/360)*(ca) ;add +nn next to the ca in (ca) at the end of
   yy=h/2+Sin((2*((angle(ca)+nn)*182))*Pi/360)*(ca) ;these lines, for another effect
   Plot xx,yy
  Next
  angle(ca)=angle(ca)+ca
 Next
End Function