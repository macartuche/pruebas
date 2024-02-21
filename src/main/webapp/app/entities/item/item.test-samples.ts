import { IItem, NewItem } from './item.model';

export const sampleWithRequiredData: IItem = {
  id: 638,
  codigo: 'lively',
  codigoCatalogo: 'intelligent',
};

export const sampleWithPartialData: IItem = {
  id: 19819,
  codigo: 'scram',
  codigoCatalogo: 'brr',
};

export const sampleWithFullData: IItem = {
  id: 31554,
  codigo: 'rude',
  codigoCatalogo: 'before',
  descripcion: 'knavishly candle',
};

export const sampleWithNewData: NewItem = {
  codigo: 'nor washtub',
  codigoCatalogo: 'equally outside outstanding',
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
