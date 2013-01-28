#include "uml_object.h"

UMLObject::UMLObject()
	: PORT_SIZE( 6.0 )
{
	this->setFlags( QGraphicsItem::ItemIsSelectable | 
					QGraphicsItem::ItemIsMovable | 
					QGraphicsItem::ItemSendsGeometryChanges );

	_topPort.setParentUMLObj( this );
	_bottomPort.setParentUMLObj( this );
	_leftPort.setParentUMLObj( this );
	_rightPort.setParentUMLObj( this );
}

void UMLObject::paint( QPainter * painter, const QStyleOptionGraphicsItem * option, QWidget * widget )
{
	if( this->isSelected() ) {
		const QRectF rect( this->boundingRect() );
		const QPointF center( rect.center() );
		
		QPainterPath path;
		path.addRect( center.x() - PORT_SIZE / 2, rect.top(), PORT_SIZE, PORT_SIZE );
		path.addRect( center.x() - PORT_SIZE / 2, rect.bottom() - PORT_SIZE, PORT_SIZE, PORT_SIZE );
		path.addRect( rect.left(), center.y() - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE );
		path.addRect( rect.right() - PORT_SIZE, center.y() - PORT_SIZE / 2, PORT_SIZE, PORT_SIZE );
		painter->fillPath( path, QColor( "darkRed" ) );
	}

	_topPort.updateLinks();
	_bottomPort.updateLinks();
	_leftPort.updateLinks();
	_rightPort.updateLinks();
}

UMLObjectPort * UMLObject::getSelectedPort( const QPointF & scenePosition )
{
	QRectF rect( this->boundingRect() );
	QPointF center( rect.center() );
	QPointF diff( this->mapFromScene( scenePosition ) - center );
	
	bool isVertical = ( fabs( diff.x() ) * 2 ) / rect.width() < ( fabs( diff.y() ) * 2 ) / rect.height();
	
	if( isVertical ) {
		return ( diff.y() < 0.0 ) ? &_topPort : &_bottomPort;
	} else {
		return ( diff.x() < 0.0 ) ? &_leftPort : &_rightPort;
	}
}
