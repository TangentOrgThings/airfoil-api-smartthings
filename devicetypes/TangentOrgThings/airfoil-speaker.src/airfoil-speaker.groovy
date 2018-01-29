// vim :set ts=2 sw=2 sts=2 expandtab smarttab :
/*
The MIT License (MIT)

Copyright (c) 2018 Brian Aker <brian@tangent.org>
Copyright (c) 2015 Jesse Newland

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in
all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
THE SOFTWARE.
 */

/* 
Forked from https://github.com/jnewland/airfoil-api-smartthings
*/
metadata {
  definition (name: "Airfoil Speaker", namespace: "TangentOrgThings", author: "Jesse Newland") {
    capability "Switch"
    capability "Switch level"
    capability "Refresh"
    command "refresh"
  }

  // UI tile definitions
  tiles {
    standardTile("switch", "device.switch", width: 2, height: 2, canChangeIcon: true) {
      state "off", label: '${name}', action: "switch.on", icon: "st.Electronics.electronics16", backgroundColor: "#ffffff"
      state "on", label: '${name}', action: "switch.off", icon: "st.Electronics.electronics16", backgroundColor: "#79b821"
    }
    standardTile("refresh", "device.switch", inactiveLabel: false, decoration: "flat") {
      state "default", label:"", action:"refresh.refresh", icon:"st.secondary.refresh"
    }
    controlTile("levelSliderControl", "device.level", "slider", height: 1, width: 2, inactiveLabel: false, range:"(0..100)") {
      state "level", action:"switch level.setLevel"
    }
    valueTile("level", "device.level", inactiveLabel: false, decoration: "flat") {
      state "level", label: 'Level ${currentValue}%'
    }

    main(["switch"])
    details(["switch", "levelSliderControl", "refresh"])  }
}

def parse(description) {
  log.debug "parse() - $description"
  def results = []

  def map = description
  if (description instanceof String)  {
    log.debug "stringToMap - ${map}"
    map = stringToMap(description)
  }

  if (map?.name && map?.value) {
    results << createEvent(name: "${map?.name}", value: "${map?.value}")
  }
  results
}

// handle commands
def on() {
  parent.on(this)
  sendEvent(name: "switch", value: "on")
}

def off() {
  parent.off(this)
  sendEvent(name: "switch", value: "off")
}

def setLevel(level) {
  log.debug "Executing 'setLevel'"
  log.debug "level=${level}"
  def percent = level / 100.00
  log.debug "percent=${percent}"
  parent.setLevel(this, percent)
  sendEvent(name: "level", value: level)
}

def refresh() {
  log.debug "Executing 'refresh'"
  parent.poll()
}
