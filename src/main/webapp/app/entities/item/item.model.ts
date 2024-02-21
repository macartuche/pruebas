import { ICatalogo } from 'app/entities/catalogo/catalogo.model';

export interface IItem {
  id: number;
  codigo?: string | null;
  codigoCatalogo?: string | null;
  descripcion?: string | null;
  catalogo?: Pick<ICatalogo, 'id'> | null;
}

export type NewItem = Omit<IItem, 'id'> & { id: null };
