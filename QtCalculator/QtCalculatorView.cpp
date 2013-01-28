#include "QtCalculatorView.h"

QtCalculatorView::QtCalculatorView(QtCalculatorModel *newModel) {
    QWidget *calculatorWindow = new QWidget;
    this->calculatorForm = new Ui::Form;
    this->model = newModel;

    calculatorForm->setupUi(calculatorWindow);
    calculatorWindow->setFixedSize(calculatorWindow->size());

    QObject::connect(this->calculatorForm->pushButton, SIGNAL(clicked()), this, SLOT(digit_7_Action()));
    QObject::connect(this->calculatorForm->pushButton_2, SIGNAL(clicked()), this, SLOT(digit_8_Action()));
    QObject::connect(this->calculatorForm->pushButton_3, SIGNAL(clicked()), this, SLOT(digit_9_Action()));
    QObject::connect(this->calculatorForm->pushButton_4, SIGNAL(clicked()), this, SLOT(digit_4_Action()));
    QObject::connect(this->calculatorForm->pushButton_5, SIGNAL(clicked()), this, SLOT(digit_5_Action()));
    QObject::connect(this->calculatorForm->pushButton_6, SIGNAL(clicked()), this, SLOT(digit_6_Action()));
    QObject::connect(this->calculatorForm->pushButton_7, SIGNAL(clicked()), this, SLOT(digit_1_Action()));
    QObject::connect(this->calculatorForm->pushButton_8, SIGNAL(clicked()), this, SLOT(digit_2_Action()));
    QObject::connect(this->calculatorForm->pushButton_9, SIGNAL(clicked()), this, SLOT(digit_3_Action()));
    QObject::connect(this->calculatorForm->pushButton_10, SIGNAL(clicked()), this, SLOT(digit_0_Action()));
    QObject::connect(this->calculatorForm->pushButton_11, SIGNAL(clicked()), this, SLOT(plusAction()));
    QObject::connect(this->calculatorForm->pushButton_12, SIGNAL(clicked()), this, SLOT(minusAction()));
    QObject::connect(this->calculatorForm->pushButton_13, SIGNAL(clicked()), this, SLOT(multiplyAction()));
    QObject::connect(this->calculatorForm->pushButton_14, SIGNAL(clicked()), this, SLOT(divideAction()));
    QObject::connect(this->calculatorForm->pushButton_15, SIGNAL(clicked()), this, SLOT(clearAction()));
    QObject::connect(this->calculatorForm->pushButton_16, SIGNAL(clicked()), this, SLOT(equalAction()));

    calculatorWindow->show();
}

void QtCalculatorView::operationAction(int functionStatus, int waitingForInput) {
    this->model->compute();
    this->model->setFunctionStatus(functionStatus);
    this->model->setWaitingForInput(waitingForInput);
    this->calculatorForm->label->setNum(this->model->getResult());
}

void QtCalculatorView::plusAction() {
    operationAction(STATUS_PLUS, TRUE);
}

void QtCalculatorView::minusAction() {
    operationAction(STATUS_MINUS, TRUE);
}

void QtCalculatorView::multiplyAction() {
    operationAction(STATUS_MUL, TRUE);
}

void QtCalculatorView::divideAction() {
    operationAction(STATUS_DIV, TRUE);
}

void QtCalculatorView::clearAction() {
    this->model->clear();
    this->model->setFunctionStatus(STATUS_NONE);
    this->model->setWaitingForInput(FALSE);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::equalAction() {
    this->model->compute();
    this->calculatorForm->label->setNum(this->model->getResult());
}

void QtCalculatorView::digit_0_Action() {
    this->model->setValue(0);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_1_Action() {
    this->model->setValue(1);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_2_Action() {
    this->model->setValue(2);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_3_Action() {
    this->model->setValue(3);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_4_Action() {
    this->model->setValue(4);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_5_Action() {
    this->model->setValue(5);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_6_Action() {
    this->model->setValue(6);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_7_Action() {
    this->model->setValue(7);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_8_Action() {
    this->model->setValue(8);
    this->calculatorForm->label->setNum(this->model->getValue());
}

void QtCalculatorView::digit_9_Action() {
    this->model->setValue(9);
    this->calculatorForm->label->setNum(this->model->getValue());
}

