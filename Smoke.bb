EndGraphics

; -----------------------------------------------------------------------------
; Example of a windowed-mode game loop...
; -----------------------------------------------------------------------------

; Event definitions...

Const EVENT_None		= $0		; No event (eg. a WaitEvent timeout)
Const EVENT_KeyDown		= $101		; Key pressed
Const EVENT_KeyUp		= $102		; Key released
Const EVENT_ASCII		= $103		; ASCII key pressed
Const EVENT_MouseDown	= $201		; Mouse button pressed
Const EVENT_MouseUp		= $202		; Mouse button released
Const EVENT_MouseMove	= $203		; Mouse moved
Const EVENT_Gadget		= $401		; Gadget clicked
Const EVENT_Move		= $801		; Window moved
Const EVENT_Size		= $802		; Window resized
Const EVENT_Close		= $803		; Window closed
Const EVENT_Front		= $804		; Window brought to front
Const EVENT_Menu		= $1001		; Menu item selected
Const EVENT_LostFocus	= $2001		; App lost focus
Const EVENT_GotFocus	= $2002		; App got focus
Const EVENT_Timer		= $4001		; Timer event occurred

; Particle type for demo purposes...

Type Particle
	Field x#
	Field y#
	Field ys#
	Field r#
	Field g#
	Field b#
	Field dec#
	Field size#
End Type

Global gh = GadgetHeight(Desktop())
Global gw = GadgetWidth(Desktop())

; Gravity...
Const grav# = 0.25

; Create a centered game window (see CenterWindow function at bottom)...
window = CreateWindow("Screen Saver",0,0,gw, gh,0,0)
canvas = CreateCanvas(0,0,gw, gh, window)

; Draw to the canvas from here on...

SetBuffer CanvasBuffer(canvas)

; Create a timer to update every 1/60th of a second...

fps = CreateTimer (60)

; Main game loop...
mouse_x = MouseX()
mouse_y = MouseY()
vx = 0
vy = 0
x = gw/2
y = gh - 10

HidePointer(canvas)

SeedRnd(MilliSecs())

Repeat

	; Event loop:
	
	; Repeat event checking until we get a timer event from
	; the 'fps' timer, every 1/60th of a second...
	
	; A MouseDown or MouseUp event sets a variable used further
	; down in the game loop...
	
	If mouse_x <> MouseX() Or mouse_y <> MouseY() Then End
	
	Repeat
		e = WaitEvent ()
		If e = $101 Or e = $201 Or e = $204 Then End
	Until e = $4001
	
	; Clear the canvas...
		
	Cls
	
	; Create a particle if the mdown variable was set in the event loop...
	vx = vx + Rand(-1, 1)
	vy = vy + Rand(-1, 1)
	x = x + vx
	y = y + vy
	If x > gw Then vx = vx - 1
	If x < 0 Then vx = vx + 1
	If y > gh - Int(gh / 50) Then vy = vy - 1
	If y < gh - Int(gh / 2) Then vy = vy + 1
	If vx > Int(gw / 200) Then vx = vx - 1
	If vx < -Int(gw / 200) Then vx = vx + 1
	If vy > Int(gw / 200) Then vy = vy - 1
	If vy < -Int(gw / 200) Then vy = vy + 1
	mx = x + Rand (-8, 8)
	my = y + Rand (-8, 8)
	CreateParticle (mx, my)
	
	; Update the particles' positions and draw them...
	
	UpdateParticles (gh)

	; Show the result...
	
	FlipCanvas canvas

Forever

; Misc functions...
Function CreateParticle (x, y)
	p.Particle = New Particle
	p\size = 0
	p\x = x
	p\y = y
	p\r = 255
	p\g = 255
	p\b = 255
	p\dec = Rnd (1, 3)
End Function

Function UpdateParticles (maxy)
	For p.Particle = Each Particle
		p\size = p\size + 0.5
		p\ys = p\ys - grav
		p\y = p\y + p\ys
		p\r = p\r - (p\dec / 4)
		p\g = p\g - (p\dec / 2): If p\g < 0 Then p\g = 0
		p\b = p\b - p\dec: If p\b < 0 Then p\b = 0
		Color p\r, p\g, p\b
		If (p\r < 0) Or (p\y > maxy)
			Delete p
		Else
			Oval p\x, p\y, p\size, p\size
		EndIf
	Next
End Function