/********************************************************************************
** Form generated from reading UI file 'main_window.ui'
**
** Created: Mon Jan 7 13:49:28 2013
**      by: Qt User Interface Compiler version 4.8.2
**
** WARNING! All changes made in this file will be lost when recompiling UI file!
********************************************************************************/

#ifndef UI_MAIN_WINDOW_H
#define UI_MAIN_WINDOW_H

#include <QtCore/QVariant>
#include <QtGui/QAction>
#include <QtGui/QApplication>
#include <QtGui/QButtonGroup>
#include <QtGui/QGraphicsView>
#include <QtGui/QHeaderView>
#include <QtGui/QMainWindow>
#include <QtGui/QMenu>
#include <QtGui/QMenuBar>
#include <QtGui/QPushButton>
#include <QtGui/QWidget>

QT_BEGIN_NAMESPACE

class Ui_MainWindow
{
public:
    QAction *actionExit;
    QAction *actionChange_Object_Name;
    QAction *actionGroup;
    QAction *actionUngroup;
    QWidget *centralWidget;
    QPushButton *select_Button;
    QPushButton *ass_line_Button;
    QPushButton *gen_line_Button;
    QPushButton *compo_Button;
    QPushButton *class_Button;
    QPushButton *use_case_Button;
    QGraphicsView *graphicsView;
    QMenuBar *menuBar;
    QMenu *menuFile;
    QMenu *menuEdit;

    void setupUi(QMainWindow *MainWindow)
    {
        if (MainWindow->objectName().isEmpty())
            MainWindow->setObjectName(QString::fromUtf8("MainWindow"));
        MainWindow->resize(867, 550);
        actionExit = new QAction(MainWindow);
        actionExit->setObjectName(QString::fromUtf8("actionExit"));
        actionChange_Object_Name = new QAction(MainWindow);
        actionChange_Object_Name->setObjectName(QString::fromUtf8("actionChange_Object_Name"));
        actionGroup = new QAction(MainWindow);
        actionGroup->setObjectName(QString::fromUtf8("actionGroup"));
        actionUngroup = new QAction(MainWindow);
        actionUngroup->setObjectName(QString::fromUtf8("actionUngroup"));
        centralWidget = new QWidget(MainWindow);
        centralWidget->setObjectName(QString::fromUtf8("centralWidget"));
        select_Button = new QPushButton(centralWidget);
        select_Button->setObjectName(QString::fromUtf8("select_Button"));
        select_Button->setGeometry(QRect(20, 40, 111, 41));
        select_Button->setCheckable(true);
        select_Button->setChecked(false);
        select_Button->setAutoExclusive(false);
        ass_line_Button = new QPushButton(centralWidget);
        ass_line_Button->setObjectName(QString::fromUtf8("ass_line_Button"));
        ass_line_Button->setGeometry(QRect(20, 110, 111, 41));
        ass_line_Button->setLayoutDirection(Qt::LeftToRight);
        ass_line_Button->setCheckable(true);
        ass_line_Button->setChecked(true);
        ass_line_Button->setAutoExclusive(false);
        gen_line_Button = new QPushButton(centralWidget);
        gen_line_Button->setObjectName(QString::fromUtf8("gen_line_Button"));
        gen_line_Button->setGeometry(QRect(20, 180, 111, 41));
        gen_line_Button->setCheckable(false);
        gen_line_Button->setChecked(false);
        gen_line_Button->setAutoExclusive(false);
        compo_Button = new QPushButton(centralWidget);
        compo_Button->setObjectName(QString::fromUtf8("compo_Button"));
        compo_Button->setGeometry(QRect(20, 250, 111, 41));
        compo_Button->setCheckable(true);
        compo_Button->setAutoExclusive(true);
        class_Button = new QPushButton(centralWidget);
        class_Button->setObjectName(QString::fromUtf8("class_Button"));
        class_Button->setGeometry(QRect(20, 320, 111, 41));
        class_Button->setCheckable(true);
        class_Button->setAutoExclusive(false);
        use_case_Button = new QPushButton(centralWidget);
        use_case_Button->setObjectName(QString::fromUtf8("use_case_Button"));
        use_case_Button->setGeometry(QRect(20, 390, 111, 41));
        use_case_Button->setCheckable(true);
        use_case_Button->setAutoExclusive(false);
        graphicsView = new QGraphicsView(centralWidget);
        graphicsView->setObjectName(QString::fromUtf8("graphicsView"));
        graphicsView->setGeometry(QRect(150, 0, 711, 521));
        MainWindow->setCentralWidget(centralWidget);
        menuBar = new QMenuBar(MainWindow);
        menuBar->setObjectName(QString::fromUtf8("menuBar"));
        menuBar->setGeometry(QRect(0, 0, 867, 22));
        menuFile = new QMenu(menuBar);
        menuFile->setObjectName(QString::fromUtf8("menuFile"));
        menuEdit = new QMenu(menuBar);
        menuEdit->setObjectName(QString::fromUtf8("menuEdit"));
        MainWindow->setMenuBar(menuBar);

        menuBar->addAction(menuFile->menuAction());
        menuBar->addAction(menuEdit->menuAction());
        menuFile->addAction(actionExit);
        menuEdit->addAction(actionChange_Object_Name);
        menuEdit->addAction(actionGroup);
        menuEdit->addAction(actionUngroup);

        retranslateUi(MainWindow);

        QMetaObject::connectSlotsByName(MainWindow);
    } // setupUi

    void retranslateUi(QMainWindow *MainWindow)
    {
        MainWindow->setWindowTitle(QApplication::translate("MainWindow", "UMLEditor", 0, QApplication::UnicodeUTF8));
        actionExit->setText(QApplication::translate("MainWindow", "Exit", 0, QApplication::UnicodeUTF8));
        actionChange_Object_Name->setText(QApplication::translate("MainWindow", "Change Object Name", 0, QApplication::UnicodeUTF8));
        actionGroup->setText(QApplication::translate("MainWindow", "Group", 0, QApplication::UnicodeUTF8));
        actionUngroup->setText(QApplication::translate("MainWindow", "Ungroup", 0, QApplication::UnicodeUTF8));
        select_Button->setText(QApplication::translate("MainWindow", "Select", 0, QApplication::UnicodeUTF8));
        ass_line_Button->setText(QApplication::translate("MainWindow", "Association line", 0, QApplication::UnicodeUTF8));
        gen_line_Button->setText(QApplication::translate("MainWindow", "Generalization Line", 0, QApplication::UnicodeUTF8));
        compo_Button->setText(QApplication::translate("MainWindow", "Composition", 0, QApplication::UnicodeUTF8));
        class_Button->setText(QApplication::translate("MainWindow", "Class", 0, QApplication::UnicodeUTF8));
        use_case_Button->setText(QApplication::translate("MainWindow", "Use Case", 0, QApplication::UnicodeUTF8));
        menuFile->setTitle(QApplication::translate("MainWindow", "File", 0, QApplication::UnicodeUTF8));
        menuEdit->setTitle(QApplication::translate("MainWindow", "Edit", 0, QApplication::UnicodeUTF8));
    } // retranslateUi

};

namespace Ui {
    class MainWindow: public Ui_MainWindow {};
} // namespace Ui

QT_END_NAMESPACE

#endif // UI_MAIN_WINDOW_H
