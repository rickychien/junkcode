#ifndef MAIN_WINDOW_H
#define MAIN_WINDOW_H

#include <QtGui/QMainWindow>

#include "ui_main_window.h"
#include "uml_scene.h"
#include "uml_class.h"

#include <QList>

class MainWindow: public QMainWindow
{
	Q_OBJECT

public:
	MainWindow( QWidget * parent = 0, Qt::WFlags flags = 0 );
	~MainWindow();

public slots:
    void onChangeNameClicked();
    void onGroupClicked();
    void onUngroupClicked();
    void onSelectButtonClicked();
    void onAssociationLineButtonClicked();
    void onGeneralizaionLineButtonClicked();
    void onCompositionLineButtonClicked();
    void onClassButtonClicked();
    void onUseCaseButtonClicked();

protected:
	void resizeEvent( QResizeEvent * event );

private:
	void initButton();
	void checkedAllButtonGroup( bool val );

	Ui::MainWindow ui;
    UMLScene * _umlScene;
	QList< QPushButton * > _buttonList;
};

#endif // MAIN_WINDOW_H
