import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 26394,
  codigo: 'without brr ethical',
  codigoCatalogo: 'behest brr which',
};

export const sampleWithPartialData: IItem = {
  id: 23265,
  codigo: 'before',
  codigoCatalogo: 'knavishly candle',
};

export const sampleWithFullData: IItem = {
  id: 19209,
  codigo: 'where pace',
  codigoCatalogo: 'horse misbehave flashy',
  descripcion: 'which collaboration blanket',
};

export const sampleWithNewData: NewItem = {
  codigo: 'rehospitalization genuine near',
  codigoCatalogo: 'captain',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
