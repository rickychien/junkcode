#include "uml_link.h"
#include "uml_object_port.h"
#include "uml_object.h"
#include "uml_link.h"

const double PI = acos( -1.0 );
const qreal ARROW_HEAD_SIZE = 20;

UMLLink::UMLLink()
{
	this->setFlags( QGraphicsItem::ItemIsSelectable | 
					QGraphicsItem::ItemIsMovable | 
					QGraphicsItem::ItemSendsGeometryChanges );
}

UMLLink::UMLLink( UMLObjectPort * startPort, UMLObjectPort * endPort )
	: _startPort( startPort )
	, _endPort( endPort )
{
	QPen pen( this->pen() );
	pen.setColor( "darkRed" );
	this->setPen( pen );
	this->setZValue( -1 );
}

void UMLLink::drawLink()
{
	QPointF startPoint = _startPort->getParentUMLObj()->scenePos() + _startPort->getOffset();
	QPointF endPoint = _endPort->getParentUMLObj()->scenePos() + _endPort->getOffset();

	QLineF line( startPoint, endPoint );
	this->setLine( line );
}
