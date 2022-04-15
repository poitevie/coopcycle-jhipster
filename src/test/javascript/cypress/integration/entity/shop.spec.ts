import { entityItemSelector } from '../../support/commands';
import {
  entityTableSelector,
  entityDetailsButtonSelector,
  entityDetailsBackButtonSelector,
  entityCreateButtonSelector,
  entityCreateSaveButtonSelector,
  entityCreateCancelButtonSelector,
  entityEditButtonSelector,
  entityDeleteButtonSelector,
  entityConfirmDeleteButtonSelector,
} from '../../support/entity';

describe('Shop e2e test', () => {
  const shopPageUrl = '/shop';
  const shopPageUrlPattern = new RegExp('/shop(\\?.*)?$');
  const username = Cypress.env('E2E_USERNAME') ?? 'user';
  const password = Cypress.env('E2E_PASSWORD') ?? 'user';
  const shopSample = { addressS: 'Expressway Innovative' };

  let shop: any;

  beforeEach(() => {
    cy.login(username, password);
  });

  beforeEach(() => {
    cy.intercept('GET', '/api/shops+(?*|)').as('entitiesRequest');
    cy.intercept('POST', '/api/shops').as('postEntityRequest');
    cy.intercept('DELETE', '/api/shops/*').as('deleteEntityRequest');
  });

  afterEach(() => {
    if (shop) {
      cy.authenticatedRequest({
        method: 'DELETE',
        url: `/api/shops/${shop.id}`,
      }).then(() => {
        shop = undefined;
      });
    }
  });

  it('Shops menu should load Shops page', () => {
    cy.visit('/');
    cy.clickOnEntityMenuItem('shop');
    cy.wait('@entitiesRequest').then(({ response }) => {
      if (response!.body.length === 0) {
        cy.get(entityTableSelector).should('not.exist');
      } else {
        cy.get(entityTableSelector).should('exist');
      }
    });
    cy.getEntityHeading('Shop').should('exist');
    cy.url().should('match', shopPageUrlPattern);
  });

  describe('Shop page', () => {
    describe('create button click', () => {
      beforeEach(() => {
        cy.visit(shopPageUrl);
        cy.wait('@entitiesRequest');
      });

      it('should load create Shop page', () => {
        cy.get(entityCreateButtonSelector).click();
        cy.url().should('match', new RegExp('/shop/new$'));
        cy.getEntityCreateUpdateHeading('Shop');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shopPageUrlPattern);
      });
    });

    describe('with existing value', () => {
      beforeEach(() => {
        cy.authenticatedRequest({
          method: 'POST',
          url: '/api/shops',
          body: shopSample,
        }).then(({ body }) => {
          shop = body;

          cy.intercept(
            {
              method: 'GET',
              url: '/api/shops+(?*|)',
              times: 1,
            },
            {
              statusCode: 200,
              body: [shop],
            }
          ).as('entitiesRequestInternal');
        });

        cy.visit(shopPageUrl);

        cy.wait('@entitiesRequestInternal');
      });

      it('detail button click should load details Shop page', () => {
        cy.get(entityDetailsButtonSelector).first().click();
        cy.getEntityDetailsHeading('shop');
        cy.get(entityDetailsBackButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shopPageUrlPattern);
      });

      it('edit button click should load edit Shop page', () => {
        cy.get(entityEditButtonSelector).first().click();
        cy.getEntityCreateUpdateHeading('Shop');
        cy.get(entityCreateSaveButtonSelector).should('exist');
        cy.get(entityCreateCancelButtonSelector).click();
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shopPageUrlPattern);
      });

      it('last delete button click should delete instance of Shop', () => {
        cy.intercept('GET', '/api/shops/*').as('dialogDeleteRequest');
        cy.get(entityDeleteButtonSelector).last().click();
        cy.wait('@dialogDeleteRequest');
        cy.getEntityDeleteDialogHeading('shop').should('exist');
        cy.get(entityConfirmDeleteButtonSelector).click();
        cy.wait('@deleteEntityRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(204);
        });
        cy.wait('@entitiesRequest').then(({ response }) => {
          expect(response!.statusCode).to.equal(200);
        });
        cy.url().should('match', shopPageUrlPattern);

        shop = undefined;
      });
    });
  });

  describe('new Shop page', () => {
    beforeEach(() => {
      cy.visit(`${shopPageUrl}`);
      cy.get(entityCreateButtonSelector).click();
      cy.getEntityCreateUpdateHeading('Shop');
    });

    it('should create an instance of Shop', () => {
      cy.get(`[data-cy="addressS"]`).type('Forward Dam').should('have.value', 'Forward Dam');

      cy.get(`[data-cy="menu"]`).type('Shoes Gorgeous service-desk').should('have.value', 'Shoes Gorgeous service-desk');

      cy.get(entityCreateSaveButtonSelector).click();

      cy.wait('@postEntityRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(201);
        shop = response!.body;
      });
      cy.wait('@entitiesRequest').then(({ response }) => {
        expect(response!.statusCode).to.equal(200);
      });
      cy.url().should('match', shopPageUrlPattern);
    });
  });
});
