Decided to learn how to do complications so this is a start. Intended for developers, and to
to install it you load it into Android Stuido and run it. Then attach it to a watch face.
Be sure to ALLOW sensor permission - this must be done once it is installed.

There are 2 complications so far, one day I'll rename directories and GIT...

Honest Heart Rate

There is also an odd glitch upon an install when activate/deactivate calls seems to be a 
bit odd. Simply change to a watch face without the complication and come back. It will work fine
thereafter.

You will notice some times it shows "0". Or on faces with a less-than-ambient mode, the icon 
does not "beat", but once the display comes out of ambient it will. I did compare side by 
side with Samsung's heart rate, and when Samsung's is updating, this one works as well.
When there is "0" or no "beat"; the Samsung complication is showing stale data. Quite frankly
I prefer to know if data is stale.

Moon Phase

This is a full image of the moon and phases. NOTE - you MUST get your own images of the moon.
I found some on the web for my use, but cannot distribute them. You should put in the drawable
directory moon0.bmp...moon23.bmp and moon24.bmp. moon2 and moon24 are new moons, the remaining
are the various phases.

NOTES

I'm making my own watch face, and I notice the monochromatic images can be colored, and for many
complications they are colored. So why not use them! Hence you will notice that for 
HonestHeartRate I have 2 styles, one that simply changes the line color, which is good for those
that can disply the image color, and one that does a line, and an inverse (square with missing
line) for those that don't.

I also use the Moon Phase as a full background image on the watch face.
