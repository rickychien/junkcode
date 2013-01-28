#include "uml_composit_line.h"
#include "uml_object.h"

const qreal ARROW_HEAD_SIZE = 8.0;

UMLCompositionLine::UMLCompositionLine()
{
	
}

UMLCompositionLine::UMLCompositionLine( UMLObjectPort * startPort, UMLObjectPort * endPort )
	: UMLLink( startPort, endPort )
{
	QPolygonF head;
	head << QPointF( 0.0, 0.0 )
		 << QPointF( 1.0, -1.0 )
		 << QPointF( 2.0, 0.0 )
		 << QPointF( 1.0, 1.0 );

	_path.addPolygon( head );
}

QRectF UMLCompositionLine::boundingRect() const 
{
	qreal extra = ( this->pen().widthF() + ARROW_HEAD_SIZE ) / 2.0;
	return this->UMLLink::boundingRect().adjusted( -extra, -extra, extra, extra );
}

void UMLCompositionLine::paint( QPainter * painter, const QStyleOptionGraphicsItem * option, QWidget * parent )
{
	QTransform transform;
	transform.translate( this->line().p2().x(), this->line().p2().y() );
	transform.scale( ARROW_HEAD_SIZE, ARROW_HEAD_SIZE );
	transform.rotate( 180.0 -this->line().angle() );
	QTransform backup( painter->transform() );
	painter->setTransform( transform, true );
	painter->fillPath( _path, QColor( "red" ) );
	painter->setTransform( backup );
	painter->drawLine( this->line().p1(), this->line().p2() );
}
