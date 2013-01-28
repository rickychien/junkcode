#include <QApplication>
#include "ui_QtCalculatorView.h"
#include "QtCalculatorView.h"
#include "QtCalculatorModel.h"

int main(int argc, char *argv[]) {
    QApplication app(argc, argv);

    QtCalculatorModel calculatorModel;
    QtCalculatorView* calculatorView = new QtCalculatorView(&calculatorModel);

    return app.exec();
}
