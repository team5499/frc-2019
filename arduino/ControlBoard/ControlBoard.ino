#include <Joystick.h>

// parameters
#define NUM_BUTTONS 12
#define NUM_JOYSTICKS 0

// ports
#define BALL_INTAKE 2
#define BALL_OUTTAKE 0

#define HATCH_INTAKE 3
#define HATCH_RELEASE 1

#define STOW 7
#define BALL_LOW 4
#define BALL_MID 9
#define BALL_HIGH 10
#define HATCH_LOW 6
#define HATCH_MID 11
#define HATCH_HIGH 8
#define BALL_HUMAN_PLAYER 5

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
  pinMode(HATCH_RELEASE, INPUT_PULLUP);
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

void checkButtonStatus(int buttonNumber, int port) {
  int buttonStatus = !digitalRead(port);
  if(buttonStatus != lastButtonState[buttonNumber]) {
    Joystick.setButton(buttonNumber, buttonStatus);
    lastButtonState[buttonNumber] = buttonStatus;
  }
}

void loop() {
  checkButtonStatus(0, BALL_INTAKE);
  checkButtonStatus(1, BALL_OUTTAKE);
  checkButtonStatus(2, HATCH_INTAKE);
  checkButtonStatus(3, HATCH_RELEASE);
  checkButtonStatus(4, STOW);
  checkButtonStatus(5, BALL_LOW);
  checkButtonStatus(6, BALL_MID);
  checkButtonStatus(7, BALL_HIGH);
  checkButtonStatus(8, HATCH_LOW);
  checkButtonStatus(9, HATCH_MID);
  checkButtonStatus(10, HATCH_HIGH);
  checkButtonStatus(11, BALL_HUMAN_PLAYER);
  delay(10);
}
