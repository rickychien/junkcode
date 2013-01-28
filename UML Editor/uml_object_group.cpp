#include "uml_object_group.h"

UMLObjectGroup::UMLObjectGroup()
{
	this->setFlags( QGraphicsItem::ItemIsMovable | 
					QGraphicsItem::ItemIsSelectable | 
					QGraphicsItem::ItemSendsGeometryChanges );
}

void UMLObjectGroup::paint( QPainter * painter, const QStyleOptionGraphicsItem * option, QWidget * widget )
{
	if( this->isSelected() ) {
		QPen pen( painter->pen() );
		pen.setColor( "gray" );
		pen.setStyle( Qt::DashLine );
		painter->setPen( pen );
		painter->drawRect( this->boundingRect() );
	}
}
