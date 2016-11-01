;isometric landscape demo
;copyright ©2000 EdzUp
;written by Ed Upton

If Trim$(CommandLine$()) <> "/S" Then End

Graphics 640,480
SetBuffer BackBuffer()

;adjust this value to adjust land height
Global LandHeight=15

Dim land(21,21)

SetupLand()

mouse_x = MouseX()
mouse_y = MouseY()

Repeat
 e = WaitEvent(1)
 If e = $101 Or e = $201 Or e = $204 Or mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
 Cls
 SetupLand()
 UpdateLand()
 Flip
Forever

Function SetupLand()
 For yy=0 To 21
  For xx=0 To 21
   land(xx,yy)=Rnd(LandHeight)
   land(xx,yy)=land(xx,yy)-Rnd(LandHeight)
  Next
 Next
End Function

Function UpdateLand()
 ;cursor position
 cx=320
 cy=150
 Color 255,255,255
 For yy=0 To 20
  ;actual x and y
  ax=cx
  ay=cy
  For xx=0 To 20 
   If xx<20 Then Line ax,ay-land(xx,yy),ax+16,(ay+8)-land(xx+1,yy)
   If yy<20 Then Line ax,ay-land(xx,yy),ax-16,(ay+8)-land(xx,yy+1)
   ax=ax+16
   ay=ay+8
  Next
  cx=cx-16
  cy=cy+8
 Next
End Function