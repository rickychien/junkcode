#include "uml_select_mode.h"

SelectMode::SelectMode()
	: _mouseSelectedBeginPoint( QPointF() )
	, _mouseSelectedRect( NULL )
{

}

SelectMode::SelectMode( UMLScene * delegateScene )
	: UMLMode( delegateScene )
	, _mouseSelectedBeginPoint( QPointF() )
	, _mouseSelectedRect( NULL )
{

}

SelectMode::~SelectMode()
{
	if ( _mouseSelectedRect ) {
		delete _mouseSelectedRect;
	}
}

void SelectMode::onMousePress( QGraphicsSceneMouseEvent * event )
{
	// If has selected top item from UML scene
	QGraphicsItem * topItem = this->delegateScene->itemAt( event->scenePos() );
	if( topItem ) {
		this->delegateScene->defaultMousePressEvent( event );
	}
	// Selected nothing from UML scene
	else {
		// When mouse press, first deselect all items
		QListIterator< QGraphicsItem * > selectedIter( this->delegateScene->selectedItems() );
		
		while( selectedIter.hasNext() ) {
			selectedIter.next()->setSelected( false );
		}
		
		// Draw mouse selected area
		_mouseSelectedRect = new QGraphicsRectItem( QRectF( event->scenePos(), QSizeF() ) );
		_mouseSelectedBeginPoint = event->scenePos();
		QPen pen( _mouseSelectedRect->pen() );
		pen.setColor( "darkRed" );
		pen.setStyle( Qt::DotLine );
		_mouseSelectedRect->setPen( pen );
		this->delegateScene->addItem( _mouseSelectedRect );
	}
}

void SelectMode::onMouseMove( QGraphicsSceneMouseEvent * event )
{
	if ( _mouseSelectedRect ) {
		QRectF rect( _mouseSelectedBeginPoint, event->scenePos() );
		_mouseSelectedRect->setRect( rect.normalized() );
	}

	this->delegateScene->defaultMouseMoveEvent( event );
}

void SelectMode::onMouseRelease( QGraphicsSceneMouseEvent * event )
{
	if ( _mouseSelectedRect ) {
		this->delegateScene->setSelectionArea( _mouseSelectedRect->mapToScene( 
											   _mouseSelectedRect->shape() ), 
											   Qt::ContainsItemBoundingRect, 
											   QTransform() );
		delete _mouseSelectedRect;
		_mouseSelectedRect = NULL;
	}

	this->delegateScene->defaultMouseReleaseEvent( event );
}
