#include <limits.h>
#include "QtCalculatorModel.h"

QtCalculatorModel::QtCalculatorModel() {
    _value = 0;
    _result = 0;
    _functionStatus = STATUS_NONE;
    _waitingForInput = TRUE;
}

int QtCalculatorModel::getValue() {
    return _value;
}

void QtCalculatorModel::setValue(int newValue) {
    _value = _value * 10 + newValue;
    _waitingForInput = FALSE;
}

int QtCalculatorModel::getResult() {
    return _result;
}

void QtCalculatorModel::setFunctionStatus(int newStatus) {
    _functionStatus = newStatus;
}

void QtCalculatorModel::setWaitingForInput(int newStatus) {
    _waitingForInput = newStatus;
}

void QtCalculatorModel::compute() {
    if (_waitingForInput == FALSE) {
        switch (_functionStatus) {
            case STATUS_NONE:
                _result = _value;
                _value = 0;
                return;
            case STATUS_PLUS:
                _result += _value;
                break;
            case STATUS_MINUS:
                _result -= _value;
                break;
            case STATUS_MUL:
                _result *= _value;
                break;
            case STATUS_DIV:
                if (_value == 0) return;
                _result /= _value;
                break;
        }

        _value = 0;
        _functionStatus = STATUS_NONE;
    }

    _waitingForInput = TRUE;
}

void QtCalculatorModel::clear() {
    _value = _result = 0;
}

