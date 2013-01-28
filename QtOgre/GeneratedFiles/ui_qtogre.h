/********************************************************************************
** Form generated from reading UI file 'qtogre.ui'
**
** Created: Mon Jan 7 13:53:57 2013
**      by: Qt User Interface Compiler version 4.8.2
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_QTOGRE_H
#define UI_QTOGRE_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QHeaderView>
#include <QtGui/QLabel>
#include <QtGui/QWidget>

QT_BEGIN_NAMESPACE

class Ui_QtOgreClass
{
public:
    QLabel *cameraLabel;
    QLabel *positionLabel;
    QWidget *widget;

    void setupUi(QWidget *QtOgreClass)
    {
        if (QtOgreClass->objectName().isEmpty())
            QtOgreClass->setObjectName(QString::fromUtf8("QtOgreClass"));
        QtOgreClass->resize(600, 400);
        cameraLabel = new QLabel(QtOgreClass);
        cameraLabel->setObjectName(QString::fromUtf8("cameraLabel"));
        cameraLabel->setGeometry(QRect(20, 10, 81, 16));
        positionLabel = new QLabel(QtOgreClass);
        positionLabel->setObjectName(QString::fromUtf8("positionLabel"));
        positionLabel->setGeometry(QRect(110, 10, 331, 16));
        widget = new QWidget(QtOgreClass);
        widget->setObjectName(QString::fromUtf8("widget"));
        widget->setGeometry(QRect(20, 30, 561, 351));

        retranslateUi(QtOgreClass);

        QMetaObject::connectSlotsByName(QtOgreClass);
    } // setupUi

    void retranslateUi(QWidget *QtOgreClass)
    {
        QtOgreClass->setWindowTitle(QApplication::translate("QtOgreClass", "Qt + Ogre", 0, QApplication::UnicodeUTF8));
        cameraLabel->setText(QApplication::translate("QtOgreClass", "Camera Position", 0, QApplication::UnicodeUTF8));
        positionLabel->setText(QApplication::translate("QtOgreClass", "(0, 0, 0)", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class QtOgreClass: public Ui_QtOgreClass {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_QTOGRE_H
