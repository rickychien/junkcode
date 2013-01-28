#include "main_window.h"

MainWindow::MainWindow( QWidget * parent, Qt::WFlags flags )
	: QMainWindow( parent, flags )
{
	ui.setupUi( this );
    _umlScene = new UMLScene();
	_umlScene->setSceneRect( ui.graphicsView->rect() );
	ui.graphicsView->setScene( _umlScene );
	_umlScene->mode = new SelectMode( _umlScene );

    QObject::connect( ui.select_Button, SIGNAL( clicked() ), this, SLOT( onSelectButtonClicked() ) );
    QObject::connect( ui.ass_line_Button , SIGNAL( clicked() ), this, SLOT( onAssociationLineButtonClicked() ) );
    QObject::connect( ui.gen_line_Button, SIGNAL( clicked() ), this, SLOT( onGeneralizaionLineButtonClicked() ) );
    QObject::connect( ui.compo_Button, SIGNAL( clicked() ), this, SLOT( onCompositionLineButtonClicked() ) );
    QObject::connect( ui.class_Button, SIGNAL( clicked() ), this, SLOT( onClassButtonClicked() ) );
    QObject::connect( ui.use_case_Button, SIGNAL( clicked() ), this, SLOT( onUseCaseButtonClicked() ) );
	QObject::connect( ui.actionExit, SIGNAL( triggered() ), this, SLOT( close() ) );
	QObject::connect( ui.actionChange_Object_Name, SIGNAL( triggered() ), this, SLOT( onChangeNameClicked() ) );
	QObject::connect( ui.actionGroup, SIGNAL( triggered() ), this, SLOT( onGroupClicked() ) );
	QObject::connect( ui.actionUngroup, SIGNAL( triggered() ), this, SLOT( onUngroupClicked() ) );

	this->initButton();
}

MainWindow::~MainWindow()
{
	delete _umlScene;
}

void MainWindow::onChangeNameClicked()
{
	_umlScene->changeUMLObjectName();
}

void MainWindow::onGroupClicked()
{
	_umlScene->groupUMLObjects();
}

void MainWindow::onUngroupClicked()
{
	_umlScene->unGroupUMLObjects();
}

void MainWindow::onSelectButtonClicked()
{
	if ( _umlScene->mode ) {
		delete _umlScene->mode;
		_umlScene->mode = NULL;
	}
	_umlScene->mode = new SelectMode( _umlScene );
	this->checkedAllButtonGroup( false );
	ui.select_Button->setChecked( true );
}

void MainWindow::onAssociationLineButtonClicked()
{
	if ( _umlScene->mode ) {
		delete _umlScene->mode;
		_umlScene->mode = NULL;
	}
	_umlScene->mode = new LinkMode( _umlScene, UMLLink::AssociationType );
	this->checkedAllButtonGroup( false );
	ui.ass_line_Button->setChecked( true );
}

void MainWindow::onGeneralizaionLineButtonClicked()
{
	if ( _umlScene->mode ) {
		delete _umlScene->mode;
		_umlScene->mode = NULL;
	}
	_umlScene->mode = new LinkMode( _umlScene, UMLLink::GeneralizationType );
	this->checkedAllButtonGroup( false );
	ui.gen_line_Button->setChecked( true );
}

void MainWindow::onCompositionLineButtonClicked()
{
	if ( _umlScene->mode ) {
		delete _umlScene->mode;
		_umlScene->mode = NULL;
	}
	_umlScene->mode = new LinkMode( _umlScene, UMLLink::CompositionType );
	this->checkedAllButtonGroup( false );
	ui.compo_Button->setChecked( true );
}

void MainWindow::onClassButtonClicked()
{
	if ( _umlScene->mode ) {
		delete _umlScene->mode;
		_umlScene->mode = NULL;
	}
	_umlScene->mode = new CreateMode( _umlScene, UMLObject::Class );
	this->checkedAllButtonGroup( false );
	ui.class_Button->setChecked( true );
}

void MainWindow::onUseCaseButtonClicked()
{
	if ( _umlScene->mode ) {
		delete _umlScene->mode;
		_umlScene->mode = NULL;
	}
	_umlScene->mode = new CreateMode( _umlScene, UMLObject::UseCase );
	this->checkedAllButtonGroup( false );
	ui.use_case_Button->setChecked( true );
}

void MainWindow::resizeEvent( QResizeEvent * event )
{
	ui.graphicsView->resize( this->width(), this->height() );
}

void MainWindow::initButton()
{
	_buttonList.push_back( ui.select_Button );
	_buttonList.push_back( ui.ass_line_Button );
	_buttonList.push_back( ui.gen_line_Button );
	_buttonList.push_back( ui.compo_Button );
	_buttonList.push_back( ui.class_Button );
	_buttonList.push_back( ui.use_case_Button );

	QListIterator< QPushButton * > iter( _buttonList );
	while( iter.hasNext() ) {
		iter.next()->setCheckable( true );
	}
}

void MainWindow::checkedAllButtonGroup( bool val )
{
	QListIterator< QPushButton * > iter( _buttonList );
	while( iter.hasNext() ) {
		iter.next()->setChecked( val );
	}
}
