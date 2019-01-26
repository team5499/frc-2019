#include<Joystick.h>

#define NUM_BUTTONS 12
#define NUM_JOYSTICKS 0

// ports
#define BALL_INTAKE 0
#define BALL_OUTTAKE 1

#define HATCH_INTAKE 2
#define HATCH_OUTTAKE 3

#define STOW 4
#define BALL_LOW 5
#define BALL_MID 6
#define BALL_HIGH 7
#define HATCH_LOW 8
#define HATCH_MID 9
#define HATCH_HIGH 10
#define BALL_HUMAN_PLAYER 11

// init joysticks
Joystick_ Joystick(JOYSTICK_DEFAULT_REPORT_ID, JOYSTICK_TYPE_GAMEPAD,
  NUM_BUTTONS, NUM_JOYSTICKS,
  false, false, false, // rest of this is random joystick config
  false, false, false,
  false, false,
  false, false, false);

int lastButtonState[NUM_BUTTONS];

void setup() {
  // setup pins
  pinMode(BALL_INTAKE, INPUT_PULLUP);
  pinMode(BALL_OUTTAKE, INPUT_PULLUP);
  pinMode(HATCH_INTAKE, INPUT_PULLUP);
  pinMode(HATCH_OUTTAKE, INPUT_PULLUP);
  pinMode(STOW, INPUT_PULLUP);
  pinMode(BALL_LOW, INPUT_PULLUP);
  pinMode(BALL_MID, INPUT_PULLUP);
  pinMode(BALL_HIGH, INPUT_PULLUP);
  pinMode(HATCH_LOW, INPUT_PULLUP);
  pinMode(HATCH_MID, INPUT_PULLUP);
  pinMode(HATCH_HIGH, INPUT_PULLUP);
  pinMode(BALL_HUMAN_PLAYER, INPUT_PULLUP);

  // start joystick
  Joystick.begin();

  for(int i : lastButtonState) {
    lastButtonState[i] = 0;
  }
}

void checkButtonStatus(int port) {
  int buttonStatus = !digitalRead(port);
  if(buttonStatus != lastButtonState[port]) {
    Joystick.setButton(port, buttonStatus);
    lastButtonState[port] = buttonStatus;
  }
}

void loop() {
  checkButtonStatus(BALL_INTAKE);
  checkButtonStatus(BALL_OUTTAKE);
  checkButtonStatus(HATCH_INTAKE);
  checkButtonStatus(HATCH_OUTTAKE);
  checkButtonStatus(STOW);
  checkButtonStatus(BALL_LOW);
  checkButtonStatus(BALL_MID);
  checkButtonStatus(BALL_HIGH);
  checkButtonStatus(HATCH_LOW);
  checkButtonStatus(HATCH_MID);
  checkButtonStatus(HATCH_HIGH);
  checkButtonStatus(BALL_HUMAN_PLAYER);
  delay(10);
}
