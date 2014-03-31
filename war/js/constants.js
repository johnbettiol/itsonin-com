angular.module('itsonin').constant('constants', {
	EVENT_TYPES: [
        {id: 'VACATION', img: 'picture', background: ''},
        {id: 'BIRTHDAY', img: 'gift', background: ''},
        {id: 'CLUBBING', img: 'glass', background: ''},
        {id: 'PICNIC', img: 'tree-conifer', background: ''},
        {id: 'DINNER', img: 'cutlery', background: ''}
    ],
    EVENT_SHARABILITIES: [
        {id: 'NOSHARE', img: 'picture'},
        {id: 'NORMAL', img: 'gift'},
        {id: 'PYRAMID', img: 'cutlery'}
    ],
    EVENT_VISIBILITIES: [
        {id: 'PUBLIC', img: 'gift'},
    	{id: 'PRIVATE', img: 'picture'},
    	{id: 'FRIENDSONLY', img: 'cutlery'}
    ],
    DATE_TYPES: [
         {id: 'EMPTY'}, 
         {id: 'NOW'}, 
         {id: 'CUSTOM'}
    ],
    LOCATION_TYPES: [
         {id: 'EMPTY'}, 
         {id: 'MAP'}
    ]
});