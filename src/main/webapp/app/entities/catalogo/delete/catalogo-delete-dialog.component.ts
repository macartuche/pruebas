import { Component } from '@angular/core';
import { FormsModule } from '@angular/forms';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import SharedModule from 'app/shared/shared.module';
import { ITEM_DELETED_EVENT } from 'app/config/navigation.constants';
import { ICatalogo } from '../catalogo.model';
import { CatalogoService } from '../service/catalogo.service';

@Component({
  standalone: true,
  templateUrl: './catalogo-delete-dialog.component.html',
  imports: [SharedModule, FormsModule],
})
export class CatalogoDeleteDialogComponent {
  catalogo?: ICatalogo;

  constructor(
    protected catalogoService: CatalogoService,
    protected activeModal: NgbActiveModal,
  ) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.catalogoService.delete(id).subscribe(() => {
      this.activeModal.close(ITEM_DELETED_EVENT);
    });
  }
}
