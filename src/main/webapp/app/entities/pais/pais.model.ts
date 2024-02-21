import { IItem } from 'app/entities/item/item.model';

export interface IPais {
  id: number;
  nombre?: string | null;
  region?: Pick<IItem, 'id'> | null;
}

export type NewPais = Omit<IPais, 'id'> & { id: null };
