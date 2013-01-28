#ifndef QTCALCULATORMODEL_H
#define QTCALCULATORMODEL_H
#include <QObject.h>

#define STATUS_NONE 0
#define STATUS_PLUS 1
#define STATUS_MINUS 2
#define STATUS_MUL 3
#define STATUS_DIV 4
#define TRUE 1
#define FALSE 0

class QtCalculatorModel : public QObject {
    Q_OBJECT;

    public:
        QtCalculatorModel();
        int getValue();
        void setValue(int newValue);
        int getResult();
        void setFunctionStatus(int newStatus);
        void setWaitingForInput(int newStatus);
        void clear();
        void compute();

    signals:
        void valueChanged(int);

    private:
        int _value;
        int _result;
        int _functionStatus;
        int _waitingForInput;
};

#endif
