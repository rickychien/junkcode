#include "uml_scene.h"
#include <QtGui/QInputDialog>
#include <QtGui/QMessageBox>
#include <QtGui/QGraphicsView>

UMLScene::UMLScene() 
	: mode( NULL )
	, _mouseSelectedRect( NULL )
	, _mouseMoveLine( NULL )
	, _startPort( NULL )
	, _startUMLObject( NULL )
{
	
}

UMLScene::~UMLScene()
{
	QListIterator< UMLObject * > umlObjectIter( umlObjectList );
	while( umlObjectIter.hasNext() ) {
		delete umlObjectIter.next();
	}

	QListIterator< UMLLink * > umlLinkIter( umlLinkList );
	while( umlLinkIter.hasNext() ) {
		delete umlLinkIter.next();
	}
}

void UMLScene::changeUMLObjectName()
{
	QList< QGraphicsItem * > selectedUMLObjectList( this->selectedItems() );
	QListIterator< QGraphicsItem * > umlObjectIter( selectedUMLObjectList );

	while( umlObjectIter.hasNext() ) {
		UMLObject * umlObject = dynamic_cast< UMLObject * >( umlObjectIter.next() );
		if ( !umlObject ) {
			QMessageBox::critical( this->views().first(), "Error",
            "Can not rename non UML object. Try to rename correct UML object.");
			return;
		}

		QString name = QInputDialog::getText( this->views().first(), 
											  QObject::tr( "Change object name" ), 
											  QObject::tr( "Enter a name" ), 
											  QLineEdit::Normal, 
											  umlObject->getName() );
		umlObject->setName( name );
	}
}

void UMLScene::defaultMousePressEvent( QGraphicsSceneMouseEvent * event )
{
	this->QGraphicsScene::mousePressEvent( event );
}

void UMLScene::defaultMouseMoveEvent( QGraphicsSceneMouseEvent * event )
{
	this->QGraphicsScene::mouseMoveEvent( event );
}

void UMLScene::defaultMouseReleaseEvent( QGraphicsSceneMouseEvent * event )
{
	this->QGraphicsScene::mouseReleaseEvent( event );
}

QList< QGraphicsItem * > UMLScene::getGroupingList()
{
	QList< QGraphicsItem * > selectedList( this->selectedItems() );
	QSet< QGraphicsItem * > roots;

	QListIterator < QGraphicsItem * > iter( selectedList );
	while( iter.hasNext() ) {
		QGraphicsItem * item = iter.next();
		QGraphicsItemGroup * itemGroup = item->group();
		while( itemGroup && itemGroup->group() ) {
			itemGroup = itemGroup->group();
		}
		
		roots.insert( itemGroup ? itemGroup : item );
	}

	return roots.toList();
}

void UMLScene::groupUMLObjects()
{
	QList< QGraphicsItem * > selectedList( this->getGroupingList() );
	if( selectedList.size() <= 1 ) return;

	UMLObjectGroup * umlObjGroup = new UMLObjectGroup();
	this->addItem( umlObjGroup );
	umlObjGroup->setSelected( true );

	QListIterator < QGraphicsItem * > iter( selectedList );
	while( iter.hasNext() ) {
		umlObjGroup->addToGroup( iter.next() );
	}
}

void UMLScene::unGroupUMLObjects()
{
	QList< QGraphicsItem * > selectedList( this->getGroupingList() );
	if( selectedList.size() != 1 ) return;

	UMLObjectGroup * umlObjGroup = dynamic_cast< UMLObjectGroup * >( selectedList.at( 0 ) );
	if( !umlObjGroup ) return;

	umlObjGroup->setSelected( false );
	selectedList = umlObjGroup->childItems();
	this->destroyItemGroup( umlObjGroup );

	QListIterator < QGraphicsItem * > iter( selectedList );
	while( iter.hasNext() ) {
		iter.next()->setSelected( false );
	}
}

void UMLScene::mousePressEvent( QGraphicsSceneMouseEvent * event )
{
	if( event->button() == Qt::LeftButton ) {
		mode->onMousePress( event );
	}
}

void UMLScene::mouseMoveEvent( QGraphicsSceneMouseEvent * event )
{
	mode->onMouseMove( event );
}

void UMLScene::mouseReleaseEvent( QGraphicsSceneMouseEvent * event )
{
	mode->onMouseRelease( event );
}
