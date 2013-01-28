#ifndef QTCALCULATORVIEW_H
#define QTCALCULATORVIEW_H
#include <QObject.h>
#include "ui_QtCalculatorView.h"
#include "QtCalculatorModel.h"

class QtCalculatorView : public QObject {
    Q_OBJECT;

    public:
        QtCalculatorView(QtCalculatorModel *model);
        void operationAction(int functionStatus, int waitingForInput);

    public slots:
        void plusAction();
        void minusAction();
        void multiplyAction();
        void divideAction();
        void clearAction();
        void equalAction();
        void digit_0_Action();
        void digit_1_Action();
        void digit_2_Action();
        void digit_3_Action();
        void digit_4_Action();
        void digit_5_Action();
        void digit_6_Action();
        void digit_7_Action();
        void digit_8_Action();
        void digit_9_Action();

    private:
        QtCalculatorModel *model;
        Ui::Form *calculatorForm;
};

#endif
