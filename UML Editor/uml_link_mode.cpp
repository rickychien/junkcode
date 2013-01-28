#include "uml_link_mode.h"

LinkMode::LinkMode()
	: UMLMode()
	, _umlLinkType( UMLLink::AssociationType )
	, _mouseMoveLine( NULL )
	, _startPort( NULL )
	, _startUMLObject( NULL )
{

}

LinkMode::LinkMode( UMLScene * delegateScene, UMLLink::Type linkType )
	: UMLMode( delegateScene )
	, _umlLinkType( linkType )
	, _mouseMoveLine( NULL )
	, _startPort( NULL )
	, _startUMLObject( NULL )
{

}

LinkMode::~LinkMode()
{
	delete _mouseMoveLine;
}

void LinkMode::onMousePress( QGraphicsSceneMouseEvent * event )
{
	// If has selected top item from UML scene
	QGraphicsItem * topItem = this->delegateScene->itemAt( event->scenePos() );
	_startUMLObject = dynamic_cast< UMLObject * >( topItem );

	if( _startUMLObject ) {
		_startPort = _startUMLObject->getSelectedPort( event->scenePos() );

		// Draw line from port to mouse cursor
		_mouseMoveLine = new QGraphicsLineItem( QLineF( event->scenePos(), event->scenePos() ) );
		QPen pen( _mouseMoveLine->pen() );
		pen.setColor( "darkRed" );
		pen.setStyle( Qt::SolidLine );
		_mouseMoveLine->setPen( pen );
		this->delegateScene->addItem( _mouseMoveLine );
	}
}

void LinkMode::onMouseMove( QGraphicsSceneMouseEvent * event )
{
	if( _mouseMoveLine ) {
		_mouseMoveLine->setLine( QLineF( _mouseMoveLine->line().p1(), event->scenePos() ) );
	}
}

void LinkMode::onMouseRelease( QGraphicsSceneMouseEvent * event )
{
	if( _mouseMoveLine ) {
		_mouseMoveLine->setLine( QLineF( _mouseMoveLine->line().p1(), event->scenePos() ) );

		delete _mouseMoveLine;
		_mouseMoveLine = NULL;

		QGraphicsItem * topItem = this->delegateScene->itemAt( event->scenePos() );
		UMLObject * topUMLObject = NULL;
		topUMLObject = dynamic_cast< UMLObject * >( topItem );

		if( topUMLObject ) {
			UMLObjectPort * endPort = topUMLObject->getSelectedPort( event->scenePos() );
			UMLLink * link;

			switch( _umlLinkType )
			{
				case UMLLink::AssociationType:
					link = new UMLAssociationLine( _startPort , endPort );
					break;
				case UMLLink::GeneralizationType:
					link = new UMLGeneralizationLine( _startPort , endPort );
					break;
				case UMLLink::CompositionType:
					link = new UMLCompositionLine( _startPort , endPort );
					break;
				default:
					break;
			}

			_startPort->addLink( link );
			endPort->addLink( link );
			this->delegateScene->addItem( link );
			link->drawLink();
			this->delegateScene->umlLinkList.push_back( link );
		}
	}
}