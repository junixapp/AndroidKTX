package com.lxj.androidktx.player


enum class PlayState(string: String){
    Idle("Idle"),
    Buffering("Buffering"),
    Ready("Ready"),
    Playing("Playing"),
    Complete("Complete"),
    Pause("Pause"),
    Error("Error")
}